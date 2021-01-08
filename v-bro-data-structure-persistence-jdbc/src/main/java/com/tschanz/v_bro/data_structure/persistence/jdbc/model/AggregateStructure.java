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
    private List<String> ownTableNames;


    public AggregateStructureNode getNodeByTableName(String tableName) {
        return this.rootNode.getNodeByTableName(tableName);
    }


    public List<RepoRelation> getFwdDepdendencyRelations() {
        return this.rootNode.getOutgoingRelations()
            .stream()
            .filter(this::isExternalRelation)
            .collect(Collectors.toList());
    }


    public List<RepoRelation> getBwdDependencyRelations() {
        return this.elementTable.getIncomingRelations()
            .stream()
            .filter(this::isExternalRelation)
            .collect(Collectors.toList());
    }


    public List<String> getOwnTableNames() {
        if (this.ownTableNames == null) {
            this.ownTableNames = this.rootNode.getRepoTables()
                .stream()
                .map(RepoTable::getName)
                .collect(Collectors.toList());
        }

        return this.ownTableNames;
    }


    public boolean isExternalRelation(RepoRelation relation) {
        if (!this.getOwnTableNames().contains(relation.getFwdClassName()) || !this.getOwnTableNames().contains(relation.getBwdClassName())) {
            return true; // links to/from external tables are ok
        } else if (!relation.getFwdClassName().equals(this.elementTable.getName())) {
            return false; // links to internal tables (except to _E) are nok
        } else if (relation.getBwdClassName().equals(this.versionTable.getName())) {
            return !relation.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME); // all links from _V, except parent-link, are nok
        } else if (this.getNodeByTableName(relation.getBwdClassName()).getLevel() > 1) {
            return true; // all tables which are not a direct child of _E, are ok
        } else {
            var relsFromSameTable = this.elementTable.getIncomingRelations()
                .stream()
                .filter(erel -> erel.getBwdClassName().equals(relation.getBwdClassName()))
                .collect(Collectors.toList());
            return (relsFromSameTable.indexOf(relation) > 0); // only allow 2nd+ link from direct _E child
        }
    }


    public boolean isElementVersionRelation(RepoRelation relation) {
        return relation.getBwdClassName().equals(this.versionTable.getName()) && relation.getBwdFieldName().equals(VersionTable.ELEMENT_ID_COLNAME);
    }
}
