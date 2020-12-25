package com.tschanz.v_bro.data_structure.domain.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class AggregateNode2 {
    @NonNull private final String name;
    @NonNull private final List<AggregateField2> fields;
    @NonNull private final List<AggregateNode2> childNodes = new ArrayList<>();


    public AggregateField2 addField(String name, String value) {
        var field = new AggregateField2(this, name, value);
        this.fields.add(field);
        return field;
    }
}

