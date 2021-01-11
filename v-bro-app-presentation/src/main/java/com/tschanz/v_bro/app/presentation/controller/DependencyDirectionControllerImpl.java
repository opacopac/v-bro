package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyDirectionItem;
import com.tschanz.v_bro.app.usecase.select_dependency_direction.SelectDependencyDirectionRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_direction.SelectDependencyDirectionUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyDirectionControllerImpl implements DependencyDirectionController {
    private final SelectDependencyDirectionUseCase selectDependencyDirectionUc;
    private final ProgressController progressController;


    public void selectDependencyDirection(DependencyDirectionItem dependencyDirectionItem) {
        if (dependencyDirectionItem == null) {
            return;
        }

        this.progressController.startProgress();

        var selectDependencyFilterRequest = new SelectDependencyDirectionRequest(dependencyDirectionItem.isFwd());
        this.selectDependencyDirectionUc.execute(selectDependencyFilterRequest);

        this.progressController.endProgress();
    }
}
