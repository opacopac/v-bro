package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.util.List;
import java.util.stream.Collectors;


public class RepoTableRecord {
    private RepoTable repoTable;
    private List<FieldValue> fieldValues;


    public RepoTable getRepoTable() { return repoTable; }
    public List<FieldValue> getFieldValues() { return fieldValues; }


    public RepoTableRecord(RepoTable repoTable, RowInfo rowInfo) {
        this.repoTable = repoTable;
        this.fieldValues = rowInfo.getAllFieldValues()
            .stream()
            .collect(Collectors.toList());
    }


    public FieldValue findFieldValue(String fieldName) {
        return this.fieldValues
            .stream()
            .filter(fieldValue -> fieldValue.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
    }
}
