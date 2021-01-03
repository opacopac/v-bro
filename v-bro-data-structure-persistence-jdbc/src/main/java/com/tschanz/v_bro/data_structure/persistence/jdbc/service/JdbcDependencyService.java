package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.Dependency;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateStructureNode;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementTable;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateData;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableJoin;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcDependencyService implements DependencyService {
    private final JdbcRepoMetadataService repoMetadataService;
    private final JdbcRepoDataService repoDataService;
    private final JdbcDataStructureService dataStructureService;
    private final JdbcElementService elementService;
    private final JdbcVersionService versionService;
    private final VersionAggregateService versionAggregateService;


    @Override
    public List<Dependency> readFwdDependencies(@NonNull VersionData version) throws RepoException {
        var aggregate = (AggregateData) this.versionAggregateService.readVersionAggregate(version); // TODO: ugly type casting

        List<Dependency> dependencies = new ArrayList<>();
        for (var record: aggregate.getAllRecords()) {
            for (var relation: record.getRepoTable().getOutgoingRelations()) {
                if (!this.isExternalRelation(relation, aggregate)) {
                    continue;
                }
                var fwdElementClassName = relation.getFwdClassName();
                var fwdElementId = record.findFieldValue(relation.getBwdFieldName()).getValueString();
                if (fwdElementId != null) {
                    var elementClass = new ElementClass(fwdElementClassName);
                    var element = new ElementData(elementClass, fwdElementId, Collections.emptyList());
                    var versions = this.versionService.readVersions(element);
                    var fwdDependency = new Dependency(elementClass, element, versions);
                    dependencies.add(fwdDependency);
                }
            }
        }

        return dependencies;
    }


    @Override
    public List<Dependency> readBwdDependencies(@NonNull ElementData element) throws RepoException {
        var elementClassName = element.getElementClass().getName();
        var elementTable = this.elementService.readElementTable(elementClassName);
        var aggregates = this.dataStructureService.getAggregateStructures();
        var bwdRelations = elementTable.getRepoTable().getIncomingRelations()
            .stream()
            .filter(rel -> !rel.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME))
            .collect(Collectors.toList());
        List<AggregateStructureNode> bwdAggregateNodes = new ArrayList<>();
        for (var bwdRelation: bwdRelations) {
            bwdAggregateNodes.addAll(aggregates
                .stream()
                .map(agg -> agg.getNodeByTableName(bwdRelation.getBwdClassName()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
            );
        }

        List<ElementData> bwdElements = new ArrayList<>();
        for (var bwdAggregateNode: bwdAggregateNodes) {
            bwdElements.addAll(this.getBwdElements(element, elementTable, bwdAggregateNode));
        }

        List<Dependency> bwdDependencies = new ArrayList<>();
        for (var bwdElement: bwdElements.stream().distinct().collect(Collectors.toList())) {
            var versions = this.versionService.readVersions(bwdElement);
            bwdDependencies.add(new Dependency(bwdElement.getElementClass(), bwdElement, versions));
        }

        return bwdDependencies;
    }


    private List<ElementData> getBwdElements(ElementData element, ElementTable elementTable, AggregateStructureNode bwdAggregateNode) throws RepoException {
        var relToElement = elementTable.getRepoTable().getIncomingRelations()
            .stream()
            .filter(rel -> rel.getBwdClassName().equals(bwdAggregateNode.getRepoTable().getName()))
            .findFirst()
            .orElse(null);
        if (relToElement != null) {
            var toElementField = bwdAggregateNode.getRepoTable().findField(relToElement.getBwdFieldName()).copyWithNewType(RepoFieldType.STRING);
            List<RepoRelation> relationChain = new ArrayList<>();
            var node = bwdAggregateNode;
            while (node != null) {
                var parentRelation = node.getParentRelation();
                if (parentRelation != null) {
                    relationChain.add(parentRelation);
                }
                node = node.getParentNode();
            }

            var bwdElementTable = bwdAggregateNode.getRootNode().getRepoTable();
            var bwdElementClass = new ElementClass(bwdElementTable.getName());
            List<RepoTableJoin> joins = new ArrayList<>();
            for (var rel: relationChain) {
                var primaryTable = this.repoMetadataService.readTableStructure(rel.getFwdClassName());
                var primaryField = primaryTable.findField(rel.getFwdFieldName());
                var secondaryTable = this.repoMetadataService.readTableStructure(rel.getBwdClassName());
                var secondaryField = secondaryTable.findField(rel.getBwdFieldName());
                joins.add(0, new RepoTableJoin(primaryTable, secondaryTable, primaryField, secondaryField));
            }
            var selectFields = List.of(bwdElementTable.findfirstIdField());
            var andFilters = List.of(new RowFilter(toElementField, RowFilterOperator.EQUALS, element.getId()));
            var rows = this.repoDataService.readRepoTableRecords(
                bwdElementTable,
                joins,
                selectFields,
                andFilters,
                Collections.emptyList(),
                -1
            );

            return rows
                .stream()
                .map(row -> new ElementData(bwdElementClass, row.findIdFieldValue().getValueString(), Collections.emptyList()))
                .collect(Collectors.toList());

        } else {
            return Collections.emptyList();
        }
    }


    private boolean isExternalRelation(RepoRelation relation, AggregateData aggregate) {
        if (this.isExternalTable(relation.getFwdClassName(), aggregate)) {
            return true; // links to external tables are ok
        } else if (!this.isElementTable(relation.getFwdClassName(), aggregate)) {
            return false; // links to internal tables, except to _E are nok
        } else if (this.isVersionTable(relation.getBwdClassName(), aggregate) && relation.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME)) {
            return false; // parent-link from _V to _E is nok
        } else {
            return true; // other internal links to _E are ok
        }
    }


    private boolean isExternalTable(String tableName, AggregateData aggregate) {
        return aggregate.getAllRecords()
            .stream()
            .noneMatch(record -> record.getRepoTable().getName().equals(tableName));
    }


    private boolean isElementTable(String tableName, AggregateData aggregate) {
        return aggregate.getElementRecord().getRecord().getRepoTable().getName().equals(tableName);
    }


    private boolean isVersionTable(String tableName, AggregateData aggregate) {
        return aggregate.getVersionRecord().getRecord().getRepoTable().getName().equals(tableName);
    }
}
