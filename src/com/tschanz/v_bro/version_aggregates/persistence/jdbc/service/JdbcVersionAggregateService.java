package com.tschanz.v_bro.version_aggregates.persistence.jdbc.service;

import com.tschanz.v_bro.elements.persistence.jdbc.service.JdbcElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.*;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoData;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.model.JdbcAggregateNode;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.model.JdbcVersionAggregate;
import com.tschanz.v_bro.versions.persistence.jdbc.service.JdbcVersionService;

import java.util.ArrayList;
import java.util.List;


public class JdbcVersionAggregateService implements VersionAggregateService {
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoData repoData;
    private final JdbcElementService elementService;
    private final JdbcVersionService versionService;


    public JdbcVersionAggregateService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData,
        JdbcRepoData repoData,
        JdbcElementService elementService,
        JdbcVersionService versionService
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
        this.repoData = repoData;
        this.elementService = elementService;
        this.versionService = versionService;
    }


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        RepoTable elementTable = this.elementService.readElementTable(elementClass);
        RepoTable versionTable = this.versionService.readVersionTable(elementTable);
        RepoTableRecord elementRecord = this.readSingleIdRecord(elementTable, Long.parseLong(elementId));
        RepoTableRecord versionRecord = this.readSingleIdRecord(versionTable, Long.parseLong(versionId));
        List<JdbcAggregateNode> childNodes = this.readChildNodes(versionRecord, List.of(elementRecord));

        return new JdbcVersionAggregate(elementRecord, versionRecord, childNodes);
    }


    private RepoTableRecord readSingleIdRecord(RepoTable repoTable, long id) throws RepoException {
        FieldValue idFieldValue = new FieldValue(repoTable.findfirstIdField(), id);
        RowFilter filter = new RowFilter(RowFilterOperator.EQUALS, idFieldValue);

        List<RepoTableRecord> records = this.repoData.readRepoTableRecords(repoTable, repoTable.getFields(), List.of(filter));
        if (records.size() != 1) {
            throw new IllegalArgumentException("multiple records for same id");
        }

        return records.get(0);
    }


    private List<JdbcAggregateNode> readChildNodes(RepoTableRecord parentRecord, List<RepoTableRecord> visitedRecords) throws RepoException {
        List<JdbcAggregateNode> childNodes = new ArrayList<>();

        for (RepoRelation relation: parentRecord.getRepoTable().getIncomingRelations()) {
            RepoTable childRepoTable = this.repoMetaData.readTableStructure(relation.getBwdClassName());
            RepoField idField = childRepoTable.findField(relation.getBwdFieldName());
            FieldValue idParentFieldValue = parentRecord.findFieldValue(relation.getFwdFieldName());
            FieldValue idFieldValue = new FieldValue(idField, idParentFieldValue.getValue());
            RowFilter filter = new RowFilter(RowFilterOperator.EQUALS, idFieldValue);
            List<RepoTableRecord> records = this.repoData.readRepoTableRecords(childRepoTable, childRepoTable.getFields(), List.of(filter));

            for (RepoTableRecord record: records) {
                childNodes.add(
                    new JdbcAggregateNode(
                        record,
                        this.readChildNodes(record, visitedRecords)
                    )
                );
            }
        }

        return childNodes;
    }
}
