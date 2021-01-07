package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyFilterItem;
import com.tschanz.v_bro.app.usecase.select_dependency_direction.SelectDependencyDirectionRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_direction.SelectDependencyDirectionUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyFilterControllerImpl implements DependencyFilterController {
    private final SelectDependencyDirectionUseCase selectDependencyDirectionUc;


    public void onDependencyFilterSelected(DependencyFilterItem dependencyFilterItem) {
        if (dependencyFilterItem == null) {
            return;
        }

        var selectDependencyFilterRequest = new SelectDependencyDirectionRequest(dependencyFilterItem.isFwd());
        this.selectDependencyDirectionUc.execute(selectDependencyFilterRequest);
    }
}
