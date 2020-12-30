package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.JdbcAggregateNode;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.JdbcVersionAggregate;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionRecord;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcVersionAggregateService implements VersionAggregateService {
    private final RepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoDataService repoData;
    private final JdbcElementService elementService;
    private final JdbcVersionService versionService;


    @Override
    public VersionAggregate readVersionAggregate(@NonNull VersionData version) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        var elementTable = this.elementService.readElementTable(version.getElement().getElementClass().getName());
        var versionTable = this.elementService.readVersionTable(elementTable);

        var elementRecord = this.elementService.readElementRecord(elementTable, Long.parseLong(version.getElement().getId()), elementTable.getFields());
        var versionRecord = !version.isEternal()
            ? this.versionService.readVersionRecord(versionTable, Long.parseLong(version.getId()), versionTable.getFields())
            : null;

        List<JdbcAggregateNode> childNodes = versionRecord != null
            ? this.readChildNodes(versionRecord)
            : Collections.emptyList();

        return new JdbcVersionAggregate(elementRecord, versionRecord, childNodes);
    }


    private List<JdbcAggregateNode> readChildNodes(VersionRecord versionRecord) throws RepoException {
        List<JdbcAggregateNode> nodes = new ArrayList<>();

        var versionTable = versionRecord.getRecord().getRepoTable();
        for (var relation: versionTable.getIncomingRelations()) {
            var ownField = versionTable.findField(relation.getFwdFieldName());
            var ownFieldValue = versionRecord.getRecord().findFieldValue(ownField.getName()).getValue();
            var childRepoTable = this.repoMetaData.readTableStructure(relation.getBwdClassName());
            var childField = childRepoTable.findField(relation.getBwdFieldName());
            var childFilter = new RowFilter(childField, RowFilterOperator.EQUALS, ownFieldValue);

            var childNodes = this.readNode(childRepoTable, childFilter);
            nodes.addAll(childNodes);
        }

        return nodes;
    }


    private List<JdbcAggregateNode> readNode(RepoTable table, RowFilter filter) throws RepoException {
        List<RepoTableRecord> records = this.repoData.readRepoTableRecords(table, Collections.emptyList(), table.getFields(), List.of(filter), Collections.emptyList(), -1);
        if (records.size() == 0) {
            return Collections.emptyList();
        }

        List<JdbcAggregateNode> childNodes = new ArrayList<>();

        for (RepoRelation relation: table.getIncomingRelations()) {
            var ownField = table.findField(relation.getFwdFieldName());
            var ownFieldValues = records
                .stream()
                .map(record -> record.findFieldValue(ownField.getName()))
                .map(FieldValue::getValue)
                .collect(Collectors.toList());
            var childRepoTable = this.repoMetaData.readTableStructure(relation.getBwdClassName());
            var childField = childRepoTable.findField(relation.getBwdFieldName());
            var childFilter = new RowFilter(childField, ownFieldValues);

            // first: bulk read children of a relation for all parent records at once (performance)
            var relationChildNodes = this.readNode(childRepoTable, childFilter);

            // second: distribute children to each each parent record separately
            for (RepoTableRecord record: records) {
                var recordChildNodes = relationChildNodes
                    .stream()
                    .filter(node -> {
                        var childFieldValue = node.getRepoTableRecord().findFieldValue(childField.getName()).getValue();
                        var ownFieldValue = record.findFieldValue(ownField.getName()).getValue();
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
