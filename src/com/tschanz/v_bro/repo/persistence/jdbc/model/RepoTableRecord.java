package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class RepoTableRecord {
    @Getter private final RepoTable repoTable;
    @Getter private final List<FieldValue> fieldValues;


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
