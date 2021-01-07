package com.tschanz.v_bro.app.usecase.select_dependency_element_filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SelectDependencyElementFilterRequest {
    private final String query;
}
