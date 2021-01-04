package com.tschanz.v_bro.app.usecase.select_dependency_element_class;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SelectDependencyElementClassRequest {
    private final String elementClassName;
}
