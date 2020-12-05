package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class FieldAggregateNodeItem {
    private final String fieldName;
    private final String fieldValue;


    @Override
    public String toString() {
        return this.fieldName + " = " + this.fieldValue;
    }
}
