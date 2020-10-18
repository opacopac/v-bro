package com.tschanz.v_bro.repo.jdbc.model;

import java.util.HashMap;
import java.util.Map;


public class RowInfo {
    private final Map<String, FieldValue> fieldValueMap;


    public Map<String, FieldValue> getFieldValueList() {
        return this.fieldValueMap;
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
