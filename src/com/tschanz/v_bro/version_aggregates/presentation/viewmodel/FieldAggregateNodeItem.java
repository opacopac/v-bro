package com.tschanz.v_bro.version_aggregates.presentation.viewmodel;


public class FieldAggregateNodeItem {
    private final String fieldName;
    private final String fieldValue;


    public String getFieldName() { return fieldName; }
    public String getFieldValue() { return fieldValue; }


    public FieldAggregateNodeItem(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }


    @Override
    public String toString() {
        return this.fieldName + " = " + this.fieldValue;
    }
}
