package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateData;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateDataNode;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionRecord;
import com.tschanz.v_bro.repo.domain.model.RepoException;
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
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcVersionAggregateService implements VersionAggregateService {
    private final JdbcRepoMetadataService repoMetadataService;
    private final JdbcRepoDataService repoDataService;
    private final JdbcElementService elementService;
    private final JdbcVersionService versionService;


    @Override
    public VersionAggregate readVersionAggregate(@NonNull VersionData version) {
        var elementTable = this.elementService.readElementTable(version.getElement().getElementClass().getName());
        var versionTable = this.elementService.readVersionTable(elementTable);

        var elementRecord = this.elementService.readElementRecord(elementTable, Long.parseLong(version.getElement().getId()), elementTable.getFields());
        var versionRecord = !version.isEternal()
            ? this.versionService.readVersionRecord(versionTable, Long.parseLong(version.getId()), versionTable.getFields())
            : null;

        List<AggregateDataNode> childNodes = versionRecord != null
            ? this.readChildNodes(versionRecord)
            : Collections.emptyList();

        return new AggregateData(elementRecord, versionRecord, childNodes);
    }


    @SneakyThrows
    private List<AggregateDataNode> readChildNodes(VersionRecord versionRecord) {
        List<AggregateDataNode> nodes = new ArrayList<>();

        var versionTable = versionRecord.getRecord().getRepoTable();
        for (var relation: versionTable.getIncomingRelations()) {
            var ownField = versionTable.findField(relation.getFwdFieldName());
            var ownFieldValue = versionRecord.getRecord().findFieldValue(ownField.getName()).getValue();
            var childRepoTable = this.repoMetadataService.readTableStructure(relation.getBwdClassName());
            var childField = childRepoTable.findField(relation.getBwdFieldName());
            var childFilter = new RowFilter(childField, RowFilterOperator.EQUALS, ownFieldValue);

            var childNodes = this.readNode(childRepoTable, childFilter);
            nodes.addAll(childNodes);
        }

        return nodes;
    }


    private List<AggregateDataNode> readNode(RepoTable table, RowFilter filter) throws RepoException {
        List<RepoTableRecord> records = this.repoDataService.readRepoTableRecords(table, Collections.emptyList(), table.getFields(), List.of(filter), Collections.emptyList(), -1);
        if (records.size() == 0) {
            return Collections.emptyList();
        }

        List<AggregateDataNode> childNodes = new ArrayList<>();

        for (RepoRelation relation: table.getIncomingRelations()) {
            var ownField = table.findField(relation.getFwdFieldName());
            var ownFieldValues = records
                .stream()
                .map(record -> record.findFieldValue(ownField.getName()))
                .map(FieldValue::getValue)
                .collect(Collectors.toList());
            var childRepoTable = this.repoMetadataService.readTableStructure(relation.getBwdClassName());
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

                childNodes.add(new AggregateDataNode(record, recordChildNodes));
            }
        }

        if (table.getIncomingRelations().size() == 0) {
            for (RepoTableRecord record: records) {
                childNodes.add(new AggregateDataNode(record, Collections.emptyList()));
            }
        }

        return childNodes;
    }
}
