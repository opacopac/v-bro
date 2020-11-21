package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
public class RowInfo {
    private final Map<String, FieldValue> fieldValueMap;


    public Collection<FieldValue> getAllFieldValues() {
        return fieldValueMap.values();
    }


    public FieldValue getFieldValue(String fieldName) {
        return this.fieldValueMap.get(fieldName);
    }
}
