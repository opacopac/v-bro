package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.util.List;


public class RepoTableRecord {
    private final RepoTable repoTable;
    private final List<FieldValue> fieldValues;


    public RepoTable getRepoTable() { return repoTable; }
    public List<FieldValue> getFieldValues() { return fieldValues; }


    public RepoTableRecord(RepoTable repoTable, List<FieldValue> fieldValues) {
        this.repoTable = repoTable;
        this.fieldValues = fieldValues;
    }


    public FieldValue findFieldValue(String fieldName) {
        return this.fieldValues
            .stream()
            .filter(fieldValue -> fieldValue.getName().toUpperCase().equals(fieldName.toUpperCase()))
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
