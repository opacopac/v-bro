package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RepoTableRecord {
    private final RepoTable repoTable;
    private final List<FieldValue> fieldValues;


    public RepoTable getRepoTable() { return repoTable; }
    public List<FieldValue> getFieldValues() { return fieldValues; }


    public RepoTableRecord(RepoTable repoTable, RowInfo rowInfo) {
        this.repoTable = repoTable;
        this.fieldValues = new ArrayList<>(rowInfo.getAllFieldValues());
    }


    public FieldValue findFieldValue(String fieldName) {
        return this.fieldValues
            .stream()
            .filter(fieldValue -> fieldValue.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
    }


    public FieldValue findIdFieldValue() {
        String idFieldName = this.repoTable.findfirstIdField().getName();
        if (idFieldName == null) {
            return null;
        } else {
            return this.findFieldValue(idFieldName);
        }
    }
}
