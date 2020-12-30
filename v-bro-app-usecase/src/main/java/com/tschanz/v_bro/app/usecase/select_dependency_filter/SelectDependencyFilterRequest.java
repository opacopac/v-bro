package com.tschanz.v_bro.app.usecase.select_dependency_filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SelectDependencyFilterRequest {
    private final boolean isFwd;
}
