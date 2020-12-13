package com.tschanz.v_bro.app.presentation.viewmodel.dependency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Getter
@RequiredArgsConstructor
public class DependencyFilterItem {
    @Accessors(fluent = true)
    private final boolean isFwd;


    @Override
    public String toString() {
        return this.isFwd ? "FWD" : "BWD";
    }
}
