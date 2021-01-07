package com.tschanz.v_bro.app.presentation.viewmodel.dependency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class DependencyFilterItem {
    private final boolean fwd;


    @Override
    public String toString() {
        return this.fwd ? "FWD" : "BWD";
    }
}
