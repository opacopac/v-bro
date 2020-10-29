package com.tschanz.v_bro.dependencies.persistence.jdbc.service;

import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoData;
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
    private final JdbcRepoData repoData;
    JdbcVersionService versionService;
    private final JdbcVersionAggregateService versionAggregateService;


    public JdbcDependencyService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData,
        JdbcRepoData repoData,
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
        JdbcVersionAggregate versionAggregate = (JdbcVersionAggregate) this.versionAggregateService.readVersionAggregate(elemenClass, elementId, versionId); // TODO: ugly type casting
        List<RepoTableRecord> records = this.getRepoTableEntries((JdbcAggregateNode) versionAggregate.getRootNode());
        RepoTableRecord element = this.getElementRecord(records);
        RepoTableRecord version = this.getVersionRecord(records);

        List<FwdDependency> dependencies = new ArrayList<>();
        for (RepoTableRecord record: records) {
            for (RepoRelation relation: record.getRepoTable().getOutgoingRelations()) {
                if (!this.isExternalRelation(relation, records)) {
                    continue;
                }
                String fwdElementClassName = relation.getFwdClassName();
                String fwdElementId = record.findFieldValue(relation.getBwdFieldName()).getValueString();
                List<VersionInfo> versionInfos = this.versionService.readVersionTimeline(fwdElementClassName, fwdElementId);
                dependencies.add(new FwdDependency(fwdElementClassName, fwdElementId, versionInfos));
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


    private RepoTableRecord getElementRecord(List<RepoTableRecord> records) {
        return records.get(0); // TODO
    }


    private RepoTableRecord getVersionRecord(List<RepoTableRecord> records) {
        return records.get(1); // TODO
    }


    private boolean isExternalRelation(RepoRelation relation, List<RepoTableRecord> records) {
        String elementTable = this.getElementRecord(records).getRepoTable().getName();
        String versionTable = this.getVersionRecord(records).getRepoTable().getName();
        String fwdElementTable = relation.getFwdClassName();

        // TODO: allow links to own _E, except parent relation from _V

        for (RepoTableRecord record: records) {
            String internalTable = record.getRepoTable().getName();
            if (fwdElementTable.equals(internalTable)) {
                return false;
            }
        }

        return true;
    }
}
