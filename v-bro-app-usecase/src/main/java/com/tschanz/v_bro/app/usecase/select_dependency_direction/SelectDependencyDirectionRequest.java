package com.tschanz.v_bro.app.usecase.select_dependency_direction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SelectDependencyDirectionRequest {
    private final boolean isFwd;
}
