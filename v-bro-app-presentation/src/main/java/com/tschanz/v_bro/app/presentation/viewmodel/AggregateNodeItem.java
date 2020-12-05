package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class AggregateNodeItem {
    private final String nodeName;
    private final List<FieldAggregateNodeItem> fieldNodes;
    private final List<AggregateNodeItem> childNodes;


    @Override
    public String toString() {
        return this.nodeName;
    }
}
