package com.tschanz.v_bro.version_aggregates.persistence.jdbc.service;

import com.tschanz.v_bro.elements.persistence.jdbc.service.JdbcElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.model.*;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.model.JdbcAggregateNode;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.model.JdbcVersionAggregate;
import com.tschanz.v_bro.versions.domain.model.VersionData;
import com.tschanz.v_bro.versions.persistence.jdbc.service.JdbcVersionService;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcVersionAggregateService implements VersionAggregateService {
    private final RepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoDataService repoData;
    private final JdbcElementService elementService;
    private final JdbcVersionService versionService;


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        RepoTable elementTable = this.elementService.readElementTable(elementClass);
        RepoTable versionTable = this.versionService.readVersionTable(elementTable);

        RepoTableRecord elementRecord = this.readSingleIdRecord(elementTable, Long.parseLong(elementId));
        RepoTableRecord versionRecord = !VersionData.ETERNAL_VERSION.getId().equals(versionId)
            ? this.readSingleIdRecord(versionTable, Long.parseLong(versionId))
            : null;

        List<JdbcAggregateNode> childNodes = versionRecord != null
            ? this.readChildNodes(versionRecord)
            : Collections.emptyList();

        return new JdbcVersionAggregate(elementRecord, versionRecord, childNodes);
    }


    private RepoTableRecord readSingleIdRecord(RepoTable repoTable, long id) throws RepoException {
        RowFilter filter = new RowFilter(repoTable.findfirstIdField(), RowFilterOperator.EQUALS, id);

        List<RepoTableRecord> records = this.repoData.readRepoTableRecords(repoTable, repoTable.getFields(), List.of(filter));
        if (records.size() != 1) {
            throw new IllegalArgumentException("multiple records for same id");
        }

        return records.get(0);
    }


    private List<JdbcAggregateNode> readChildNodes(RepoTableRecord versionRecord) throws RepoException {
        List<JdbcAggregateNode> nodes = new ArrayList<>();

        RepoTable versionTable = versionRecord.getRepoTable();
        for (RepoRelation relation: versionTable.getIncomingRelations()) {
            RepoField ownField = versionTable.findField(relation.getFwdFieldName());
            Object ownFieldValue = versionRecord.findFieldValue(ownField.getName()).getValue();
            RepoTable childRepoTable = this.repoMetaData.readTableStructure(relation.getBwdClassName());
            RepoField childField = childRepoTable.findField(relation.getBwdFieldName());
            RowFilter childFilter = new RowFilter(childField, RowFilterOperator.EQUALS, ownFieldValue);

            List<JdbcAggregateNode> childNodes = this.readNode(childRepoTable, childFilter);
            nodes.addAll(childNodes);
        }

        return nodes;
    }


    private List<JdbcAggregateNode> readNode(RepoTable table, RowFilter filter) throws RepoException {
        List<RepoTableRecord> records = this.repoData.readRepoTableRecords(table, table.getFields(), List.of(filter));
        if (records.size() == 0) {
            return Collections.emptyList();
        }

        List<JdbcAggregateNode> childNodes = new ArrayList<>();

        for (RepoRelation relation: table.getIncomingRelations()) {
            RepoField ownField = table.findField(relation.getFwdFieldName());
            List<Object> ownFieldValues = records
                .stream()
                .map(record -> record.findFieldValue(ownField.getName()))
                .map(FieldValue::getValue)
                .collect(Collectors.toList());
            RepoTable childRepoTable = this.repoMetaData.readTableStructure(relation.getBwdClassName());
            RepoField childField = childRepoTable.findField(relation.getBwdFieldName());
            RowFilter childFilter = new RowFilter(childField, ownFieldValues);

            // first: bulk read children of a relation for all parent records at once (performance)
            List<JdbcAggregateNode> relationChildNodes = this.readNode(childRepoTable, childFilter);

            // second: distribute children to each each parent record separately
            for (RepoTableRecord record: records) {
                List<JdbcAggregateNode> recordChildNodes = relationChildNodes
                    .stream()
                    .filter(node -> {
                        Object childFieldValue = node.getRepoTableRecord().findFieldValue(childField.getName()).getValue();
                        Object ownFieldValue = record.findFieldValue(ownField.getName()).getValue();
                        return childFieldValue.equals(ownFieldValue);
                    })
                    .collect(Collectors.toList());

                childNodes.add(new JdbcAggregateNode(record, recordChildNodes));
            }
        }

        if (table.getIncomingRelations().size() == 0) {
            for (RepoTableRecord record: records) {
                childNodes.add(new JdbcAggregateNode(record, Collections.emptyList()));
            }
        }


        return childNodes;
    }
}
