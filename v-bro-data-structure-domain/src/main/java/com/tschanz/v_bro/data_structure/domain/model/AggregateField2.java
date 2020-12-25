package com.tschanz.v_bro.data_structure.domain.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class AggregateField2 {
    @NonNull private final AggregateNode2 node;
    @NonNull private final String name;
    @NonNull private final String value;
}

