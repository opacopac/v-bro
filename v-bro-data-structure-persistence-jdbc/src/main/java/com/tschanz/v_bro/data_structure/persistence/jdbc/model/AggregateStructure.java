package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class AggregateStructure {
    @NonNull private final ElementTable elementTable;
    private final VersionTable versionTable;
    @NonNull private final AggregateStructureNode rootNode;


    public AggregateStructureNode getNodeByTableName(String tableName) {
        return this.rootNode.getNodeByTableName(tableName);
    }
}