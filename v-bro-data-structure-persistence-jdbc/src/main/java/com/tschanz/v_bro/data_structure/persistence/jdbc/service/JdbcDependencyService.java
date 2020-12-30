package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.Dependency;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.JdbcVersionAggregate;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
public class JdbcDependencyService implements DependencyService {
    private final JdbcElementService elementService;
    private final JdbcVersionService versionService;
    private final VersionAggregateService versionAggregateService;


    @Override
    public List<Dependency> readFwdDependencies(@NonNull VersionData version) throws RepoException {
        var aggregate = (JdbcVersionAggregate) this.versionAggregateService.readVersionAggregate(version); // TODO: ugly type casting

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
        var elementTable = this.elementService.readElementTable(element.getElementClass().getName());
        return Collections.emptyList(); // TODO
    }


    /*private void getIncomingElements(ElementTable elementTable) throws RepoException {
        elementTable.getIncomingRelations();
    }*/


    private boolean isExternalRelation(RepoRelation relation, JdbcVersionAggregate aggregate) {
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


    private boolean isExternalTable(String tableName, JdbcVersionAggregate aggregate) {
        return aggregate.getAllRecords()
            .stream()
            .noneMatch(record -> record.getRepoTable().getName().equals(tableName));
    }


    private boolean isElementTable(String tableName, JdbcVersionAggregate aggregate) {
        return aggregate.getElementRecord().getRecord().getRepoTable().getName().equals(tableName);
    }


    private boolean isVersionTable(String tableName, JdbcVersionAggregate aggregate) {
        return aggregate.getVersionRecord().getRecord().getRepoTable().getName().equals(tableName);
    }
}
