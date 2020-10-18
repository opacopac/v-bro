package com.tschanz.v_bro.repo.jdbc.querybuilder;


import com.tschanz.v_bro.repo.jdbc.model.FieldValue;

public class RowFilter {
    private final RowFilterOperator operator;
    private final FieldValue fieldValue;


    public RowFilterOperator getOperator() { return operator; }
    public FieldValue getFieldValue() { return fieldValue; }


    public RowFilter(
        RowFilterOperator operator,
        FieldValue fieldValue
    ) {
        this.operator = operator;
        this.fieldValue = fieldValue;
    }
}
