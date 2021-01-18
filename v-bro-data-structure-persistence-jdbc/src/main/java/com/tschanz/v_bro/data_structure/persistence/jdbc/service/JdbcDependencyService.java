package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateData;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateStructureNode;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementTable;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableJoin;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDate;
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
    public List<Dependency> readFwdDependencies(
        @NonNull VersionData version,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        String query,
        int maxResults
    ) {
        var aggregateStruct = this.dataStructureService.getAggregateStructureByElementClass(version.getElement().getElementClass().getName());
        var aggregateData = (AggregateData) this.versionAggregateService.readVersionAggregate(version);

        List<Dependency> dependencies = new ArrayList<>();
        List<String> processedfwdElementIds = new ArrayList<>();
        for (var record: aggregateData.getAllRecords()) {
            for (var relation: record.getRepoTable().getOutgoingRelations()) {
                if (dependencies.size() >= maxResults) {
                    break;
                }
                if (!aggregateStruct.getFwdDepdendencyRelations().contains(relation)) {
                    continue;
                }
                var fwdElementClassName = relation.getFwdClassName();
                if (elementClassFilter != null && !elementClassFilter.getName().equals(fwdElementClassName)) {
                    continue;
                }
                var fwdElementId = record.findFieldValue(relation.getBwdFieldName()).getValueString();
                if (fwdElementId != null && !processedfwdElementIds.contains(fwdElementId)) {
                    processedfwdElementIds.add(fwdElementId);
                    var elementClass = new ElementClass(fwdElementClassName);
                    var element = this.elementService.readElement(elementClass, denominations, fwdElementId);
                    if (!this.isQueryMatch(element, query)) {
                        continue;
                    }
                    var versions = this.versionService.readVersions(element, minGueltigVon, maxGueltigBis, minPflegestatus);
                    var fwdDependency = new Dependency(elementClass, element, versions);
                    dependencies.add(fwdDependency);
                }
            }
        }

        return dependencies;
    }


    @Override
    public List<Dependency> readBwdDependencies(
        @NonNull ElementData element,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        String query,
        int maxResults
    ) {
        var elementClassName = element.getElementClass().getName();
        var elementTable = this.elementService.readElementTable(elementClassName);
        var aggregateStruct = this.dataStructureService.getAggregateStructureByElementClass(elementClassName);
        var bwdRelations = aggregateStruct.getBwdDependencyRelations();
        List<AggregateStructureNode> bwdAggregateNodes = new ArrayList<>();
        for (var bwdRelation: bwdRelations) {
            bwdAggregateNodes.addAll(this.dataStructureService.getAggregateStructures()
                .stream()
                .map(agg -> agg.getNodeByTableName(bwdRelation.getBwdClassName()))
                .filter(Objects::nonNull)
                .filter(agg -> elementClassFilter == null || elementClassFilter.getName().equals(agg.getRootNode().getRepoTable().getName()))
                .collect(Collectors.toList())
            );
        }

        List<ElementData> bwdElements = new ArrayList<>();
        for (var bwdAggregateNode: bwdAggregateNodes) {
            this.addBwdElements(bwdElements, element, elementTable, bwdAggregateNode, denominations, query, maxResults);
        }

        List<Dependency> bwdDependencies = new ArrayList<>();
        for (var bwdElement: bwdElements.stream().distinct().collect(Collectors.toList())) {
            var versions = this.versionService.readVersions(bwdElement, minGueltigVon, maxGueltigBis, minPflegestatus);
            bwdDependencies.add(new Dependency(bwdElement.getElementClass(), bwdElement, versions));
        }

        return bwdDependencies;
    }


    @SneakyThrows
    private void addBwdElements(List<ElementData> bwdElements, ElementData element, ElementTable elementTable, AggregateStructureNode bwdAggregateNode, List<Denomination> denominations, String query, int maxResults) {
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
            var selectFields = List.of(bwdElementTable.findfirstPkField());
            var andFilters = List.of(new RowFilter(toElementField, RowFilterOperator.EQUALS, element.getId()));
            var rows = this.repoDataService.readRepoTableRecords(
                bwdElementTable,
                joins,
                selectFields,
                andFilters,
                Collections.emptyList(),
                -1
            );

            bwdElements.addAll(rows
                .stream()
                .map(row -> this.elementService.readElement(bwdElementClass, denominations, row.findPkFieldValue().getValueString()))
                .filter(bwdElement -> this.isQueryMatch(bwdElement, query))
                .limit(maxResults)
                .collect(Collectors.toList())
            );
        }
    }


    private boolean isQueryMatch(ElementData element, String query) {
        if (query != null && !query.isBlank()) {
            var matchCount = element.getDenominations()
                .stream()
                .filter(e -> e.getValue().toLowerCase().contains(query.toLowerCase()))
                .count();

            return matchCount > 0;
        }

        return true;
    }
}
