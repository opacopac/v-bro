package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class AggregateStructureNode {
    @NonNull private final RepoTable repoTable;
    private final RepoRelation parentRelation;
    private final AggregateStructureNode parentNode;
    private final List<AggregateStructureNode> childNodes = new ArrayList<>();


    public AggregateStructureNode getRootNode() {
        if (this.getParentNode() == null) {
            return this;
        } else {
            return this.parentNode.getRootNode();
        }
    }


    public AggregateStructureNode getNodeByTableName(String tableName) {
        if (this.getRepoTable().getName().equals(tableName)) {
            return this;
        } else {
            for (var childNode: childNodes) {
                var result = childNode.getNodeByTableName(tableName);
                if (result != null) {
                    return result;
                }
            }

            return null;
        }
    }


    public List<RepoTable> getRepoTables() {
        List<RepoTable> repoTables = new ArrayList<>();

        repoTables.add(this.repoTable);
        this.childNodes.forEach(node -> repoTables.addAll(node.getRepoTables()));

        return repoTables;
    }


    public List<RepoRelation> getOutgoingRelations() {
        List<RepoRelation> repoRelations = new ArrayList<>();
        repoRelations.addAll(this.repoTable.getOutgoingRelations());

        this.childNodes.forEach(node -> repoRelations.addAll(node.getOutgoingRelations()));

        return repoRelations;
    }
}
