package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionRequest;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionUseCase;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class DependencyListController {
    private final OpenDependencyVersionUseCase openDependencyVersionUc;


    public DependencyListController(
        ViewAction<ElementVersionVector> selectDependencyVersionAction,
        OpenDependencyVersionUseCase openDependencyVersionUc
    ) {
        this.openDependencyVersionUc = openDependencyVersionUc;

        selectDependencyVersionAction.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));
    }


    public void onVersionFilterSelected(ElementVersionVector selectedDependencyVersion) {
        if (selectedDependencyVersion == null) {
            return;
        }

        var request = new OpenDependencyVersionRequest(
            selectedDependencyVersion.getElementClass(),
            selectedDependencyVersion.getElementId(),
            selectedDependencyVersion.getVersionId()
        );
        this.openDependencyVersionUc.execute(request);
    }
}
