package com.tschanz.v_bro.app.usecase.query_elements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QueryElementsRequest {
    private final String query;
}
