package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.util.Collection;
import java.util.HashMap;
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


    public RowInfo(FieldValue... fieldValues) {
        this.fieldValueMap = new HashMap<>();
        for (FieldValue fieldValue: fieldValues) {
            this.fieldValueMap.put(fieldValue.getName(), fieldValue);
        }
    }
}
