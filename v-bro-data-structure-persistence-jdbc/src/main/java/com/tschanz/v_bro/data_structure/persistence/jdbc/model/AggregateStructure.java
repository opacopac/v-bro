package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class AggregateStructure {
    @NonNull private final ElementTable elementTable;
    private final VersionTable versionTable;
    @NonNull private final AggregateStructureNode rootNode;


    public AggregateStructureNode getNodeByTableName(String tableName) {
        return this.rootNode.getNodeByTableName(tableName);
    }


    public List<RepoRelation> getFwdDepdendencyRelations() {
        var ownTableNames = this.rootNode.getRepoTables()
            .stream()
            .map(RepoTable::getName)
            .collect(Collectors.toList());

        return this.rootNode.getOutgoingRelations()
            .stream()
            .filter(rel -> !ownTableNames.contains(rel.getFwdClassName()) || rel.getFwdClassName().equals(this.elementTable.getName()))
            .filter(rel -> !rel.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME))
            .collect(Collectors.toList());
    }


    public List<RepoRelation> getBwdDependencyRelations() {
        return this.elementTable.getIncomingRelations()
            .stream()
            .filter(rel -> !rel.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME))
            .collect(Collectors.toList());
    }
}