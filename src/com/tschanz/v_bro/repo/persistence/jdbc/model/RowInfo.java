package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.util.Collection;
import java.util.Map;


public class RowInfo {
    private final Map<String, FieldValue> fieldValueMap;


    public Collection<FieldValue> getAllFieldValues() {
        return fieldValueMap.values();
    }


    public FieldValue getFieldValue(String fieldName) {
        return this.fieldValueMap.get(fieldName);
    }


    public RowInfo(Map<String, FieldValue> fieldValueMap) {
        this.fieldValueMap = fieldValueMap;
    }
}
