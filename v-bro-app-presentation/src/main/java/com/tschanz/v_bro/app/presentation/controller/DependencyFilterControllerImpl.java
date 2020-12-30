package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyFilterItem;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyFilterControllerImpl implements DependencyFilterController {
    private final SelectDependencyFilterUseCase selectDependencyFilterUc;


    public void onDependencyFilterSelected(DependencyFilterItem dependencyFilterItem) {
        if (dependencyFilterItem == null) {
            return;
        }

        var selectDependencyFilterRequest = new SelectDependencyFilterRequest(dependencyFilterItem.isFwd());
        this.selectDependencyFilterUc.execute(selectDependencyFilterRequest);
    }
}
