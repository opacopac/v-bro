package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateStructure;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateStructureNode;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementTable;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcDataStructureService {
    @NonNull private final JdbcRepoMetadataServiceImpl repoMetadataService;
    @Getter(lazy = true) private final List<AggregateStructure> aggregateStructures = this.readAggregateStructures();


    @SneakyThrows
    private List<AggregateStructure> readAggregateStructures() {
        List<String> unprocessedTableNames = this.getAllTableNames();
        List<AggregateStructure> aggregateStructures = new ArrayList<>();

        // versioned aggregates
        for (var rel: this.repoMetadataService.getRepoRelationLut()) {
            if (rel.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME) && rel.getBwdClassName().endsWith(VersionTable.TABLE_SUFFIX)) {
                var elementTable = new ElementTable(this.repoMetadataService.readTableStructure(rel.getFwdClassName()));
                unprocessedTableNames.remove(elementTable.getName());
                var versionTable = new VersionTable(this.repoMetadataService.readTableStructure(rel.getBwdClassName()));
                unprocessedTableNames.remove(versionTable.getName());
                var rootNode = this.getRootNodeAndTree(elementTable.getRepoTable(), versionTable.getRepoTable(), unprocessedTableNames);
                aggregateStructures.add(new AggregateStructure(elementTable, versionTable, rootNode));
            }
        }

        // single table aggregates
        for (var tableName: unprocessedTableNames) {
            var repoTable = this.repoMetadataService.readTableStructure(tableName);
            if (repoTable.findAllIdFields().size() == 1) {
                var elementTable = new ElementTable(repoTable);
                var rootNode = new AggregateStructureNode(elementTable.getRepoTable(), null, null);
                aggregateStructures.add(new AggregateStructure(elementTable, null, rootNode));
            }
        }

        return aggregateStructures;
    }


    private List<String> getAllTableNames() throws RepoException {
        return this.repoMetadataService.getRepoFieldLut()
            .stream()
            .map(RepoField::getTableName)
            .distinct()
            .collect(Collectors.toList());
    }


    private AggregateStructureNode getRootNodeAndTree(RepoTable elementTable, RepoTable versionTable, List<String> unprocessedTableNames) throws RepoException {
        var elementNode = new AggregateStructureNode(elementTable, null, null);
        var versionParentRelation = versionTable.getOutgoingRelations()
            .stream()
            .filter(rel -> rel.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME))
            .findFirst()
            .orElse(null);
        var versionNode = new AggregateStructureNode(versionTable, versionParentRelation, elementNode);

        elementNode.getChildNodes().add(versionNode);
        versionNode.getChildNodes().addAll(this.getChildNodes(versionTable, versionNode, unprocessedTableNames));

        return elementNode;
    }


    private List<AggregateStructureNode> getChildNodes(RepoTable parentTable, AggregateStructureNode parentNode, List<String> unprocessedTableNames) throws RepoException {
        List<AggregateStructureNode> childNodes = new ArrayList<>();

        for (var rel: parentTable.getIncomingRelations()) {
            var repoTable = this.repoMetadataService.readTableStructure(rel.getBwdClassName());
            unprocessedTableNames.remove(repoTable.getName());
            var node = new AggregateStructureNode(repoTable, rel, parentNode);
            node.getChildNodes().addAll(this.getChildNodes(repoTable, node, unprocessedTableNames));
            childNodes.add(node);
        }

        return childNodes;
    }
}
