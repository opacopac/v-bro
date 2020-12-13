package com.tschanz.v_bro.app.presentation.viewmodel.element;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QueryElementItem {
    private final String queryText;
    private final long schedulingTimestamp;


    @Override
    public String toString() {
        return this.queryText;
    }
}
