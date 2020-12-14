package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyFilterItem;


public class DependencyFilterControllerImpl implements DependencyFilterController {
    public DependencyFilterControllerImpl() {
        // TODO: inject DependencyFilterUc
    }


    public void onDependencyFilterSelected(DependencyFilterItem dependencyFilterItem) {
        if (dependencyFilterItem == null) {
            return;
        }

        return;

        // TODO: exec DependencyFilterUc
    }
}
