package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateStructure;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.AggregateStructureNode;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementTable;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcDataStructureService {
    @NonNull private final JdbcRepoMetadataServiceImpl repoMetadataService;
    private final Map<String, AggregateStructure> aggregateStructureMap = new HashMap<>();


    public List<AggregateStructure> getAggregateStructures() {
        return new ArrayList<>(this.aggregateStructureMap.values());
    }


    public AggregateStructure getAggregateStructureByElementClass(String elementClassName) {
        return this.aggregateStructureMap.get(elementClassName);
    }


    public AggregateStructure getAggregateStructureContainingTable(String tableName) {
        return this.getAggregateStructures()
            .stream()
            .filter(agg -> agg.getNodeByTableName(tableName) != null)
            .findFirst()
            .orElse(null);
    }


    @SneakyThrows
    public void readAggregateStructures() {
        List<String> unprocessedTableNames = this.getAllTableNames();
        this.aggregateStructureMap.clear();

        // versioned aggregates (but without element children)
        for (var rel: this.repoMetadataService.getRepoRelationLut()) {
            if (rel.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME) && rel.getBwdClassName().endsWith(VersionTable.TABLE_SUFFIX)) {
                var elementTable = new ElementTable(this.repoMetadataService.readTableStructure(rel.getFwdClassName()));
                unprocessedTableNames.remove(elementTable.getName());
                var versionTable = new VersionTable(this.repoMetadataService.readTableStructure(rel.getBwdClassName()));
                unprocessedTableNames.remove(versionTable.getName());
                var rootNode = this.getRootNodeAndTree(elementTable.getRepoTable(), versionTable.getRepoTable(), unprocessedTableNames);
                this.aggregateStructureMap.put(elementTable.getName(), new AggregateStructure(elementTable, versionTable, rootNode));
            }
        }

        // single table aggregates
        for (var tableName: new ArrayList<>(unprocessedTableNames)) {
            var repoTable = this.repoMetadataService.readTableStructure(tableName);
            if (repoTable.findAllPkFields().size() == 1 && unprocessedTableNames.contains(repoTable.getName())) {
                unprocessedTableNames.remove(repoTable.getName());
                var elementTable = new ElementTable(repoTable);
                var rootNode = new AggregateStructureNode(elementTable.getRepoTable(), null, null);
                this.aggregateStructureMap.put(elementTable.getName(), new AggregateStructure(elementTable, null, rootNode));
            }
        }

        // attaching remaining element children (e.g. E-E cross tables) to aggregates
        for (var tableName: new ArrayList<>(unprocessedTableNames)) {
            var repoTable = this.repoMetadataService.readTableStructure(tableName);
            var fwdTableNames = repoTable.getOutgoingRelations().stream().map(RepoRelation::getFwdClassName).collect(Collectors.toList());
            for (var fwdTableName: fwdTableNames) {
                var aggregate = this.aggregateStructureMap.get(fwdTableName);
                if (aggregate != null && unprocessedTableNames.contains(repoTable.getName())) {
                    unprocessedTableNames.remove(repoTable.getName());
                    var parentRelation = repoTable.getOutgoingRelations().stream().filter(rel -> rel.getFwdClassName().equals(fwdTableName)).findFirst().orElse(null);
                    var elementChildNode = new AggregateStructureNode(repoTable, parentRelation, aggregate.getRootNode());
                    aggregate.getRootNode().getChildNodes().add(elementChildNode);
                    elementChildNode.getChildNodes().addAll(this.getChildNodes(repoTable, elementChildNode, unprocessedTableNames));
                }
            }
        }

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
            if (unprocessedTableNames.remove(repoTable.getName())) {
                var node = new AggregateStructureNode(repoTable, rel, parentNode);
                node.getChildNodes().addAll(this.getChildNodes(repoTable, node, unprocessedTableNames));
                childNodes.add(node);
            }
        }

        return childNodes;
    }
}
