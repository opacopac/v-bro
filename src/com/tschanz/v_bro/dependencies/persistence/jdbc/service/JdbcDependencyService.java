package com.tschanz.v_bro.dependencies.persistence.jdbc.service;

import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.model.JdbcVersionAggregate;
import com.tschanz.v_bro.versions.domain.model.VersionData;
import com.tschanz.v_bro.versions.domain.service.VersionService;
import com.tschanz.v_bro.versions.persistence.jdbc.service.JdbcVersionService;

import java.util.ArrayList;
import java.util.List;


public class JdbcDependencyService implements DependencyService {
    private final VersionService versionService;
    private final VersionAggregateService versionAggregateService;


    public JdbcDependencyService(
        VersionService versionService,
        VersionAggregateService versionAggregateService
    ) {
        this.versionService = versionService;
        this.versionAggregateService = versionAggregateService;
    }


    @Override
    public List<FwdDependency> readFwdDependencies(String elemenClass, String elementId, String versionId) throws RepoException {
        JdbcVersionAggregate aggregate = (JdbcVersionAggregate) this.versionAggregateService.readVersionAggregate(elemenClass, elementId, versionId); // TODO: ugly type casting

        List<FwdDependency> dependencies = new ArrayList<>();
        for (RepoTableRecord record: aggregate.getAllRecords()) {
            for (RepoRelation relation: record.getRepoTable().getOutgoingRelations()) {
                if (!this.isExternalRelation(relation, aggregate)) {
                    continue;
                }
                String fwdElementClassName = relation.getFwdClassName();
                String fwdElementId = record.findFieldValue(relation.getBwdFieldName()).getValueString();
                if (fwdElementId != null) {
                    List<VersionData> versionData = this.versionService.readVersionTimeline(fwdElementClassName, fwdElementId);
                    dependencies.add(new FwdDependency(fwdElementClassName, fwdElementId, versionData));
                }
            }
        }

        return dependencies;
    }


    private boolean isExternalRelation(RepoRelation relation, JdbcVersionAggregate aggregate) {
        if (this.isExternalTable(relation.getFwdClassName(), aggregate)) {
            return true; // links to external tables are ok
        } else if (!this.isElementTable(relation.getFwdClassName(), aggregate)) {
            return false; // links to internal tables, except to _E are nok
        } else if (this.isVersionTable(relation.getBwdClassName(), aggregate) && relation.getBwdFieldName().equals(JdbcVersionService.ELEMENT_ID_COLNAME)) {
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
        return aggregate.getElementRecord().getRepoTable().getName().equals(tableName);
    }


    private boolean isVersionTable(String tableName, JdbcVersionAggregate aggregate) {
        return aggregate.getVersionRecord().getRepoTable().getName().equals(tableName);
    }
}
