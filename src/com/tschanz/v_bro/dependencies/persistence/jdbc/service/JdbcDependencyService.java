package com.tschanz.v_bro.dependencies.persistence.jdbc.service;

import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.model.JdbcAggregateNode;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.model.JdbcVersionAggregate;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.service.JdbcVersionAggregateService;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.persistence.jdbc.service.JdbcVersionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class JdbcDependencyService implements DependencyService {
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoDataService repoData;
    JdbcVersionService versionService;
    private final JdbcVersionAggregateService versionAggregateService;


    public JdbcDependencyService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData,
        JdbcRepoDataService repoData,
        JdbcVersionService versionService,
        JdbcVersionAggregateService versionAggregateService
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
        this.repoData = repoData;
        this.versionService = versionService;
        this.versionAggregateService = versionAggregateService;
    }


    @Override
    public Collection<FwdDependency> readFwdDependencies(String elemenClass, String elementId, String versionId) throws RepoException {
        JdbcVersionAggregate aggregate = (JdbcVersionAggregate) this.versionAggregateService.readVersionAggregate(elemenClass, elementId, versionId); // TODO: ugly type casting

        List<FwdDependency> dependencies = new ArrayList<>();
        for (RepoTableRecord record: aggregate.getAllRecords()) {
            for (RepoRelation relation: record.getRepoTable().getOutgoingRelations()) {
                if (!this.isExternalRelation(relation, aggregate)) {
                    continue;
                }
                String fwdElementClassName = relation.getFwdClassName();
                String fwdElementId = record.findFieldValue(relation.getBwdFieldName()).getValueString();
                List<VersionInfo> versionInfos = this.versionService.readVersionTimeline(fwdElementClassName, fwdElementId);
                dependencies.add(
                    new FwdDependency(fwdElementClassName, fwdElementId, versionInfos)
                );
            }
        }

        return dependencies;
    }


    private List<RepoTableRecord> getRepoTableEntries(JdbcAggregateNode jdbcAggregateNode) {
        ArrayList<RepoTableRecord> nodes = new ArrayList<>();

        nodes.add(jdbcAggregateNode.getRepoTableEntry());
        jdbcAggregateNode.getJdbcChildNodes().forEach(childNode -> nodes.addAll(this.getRepoTableEntries(childNode)));

        return nodes;
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
