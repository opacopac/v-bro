package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionRequest;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionUseCase;


public class DependencyListControllerImpl implements DependencyListController {
    private final OpenDependencyVersionUseCase openDependencyVersionUc;


    public DependencyListControllerImpl(OpenDependencyVersionUseCase openDependencyVersionUc) {
        this.openDependencyVersionUc = openDependencyVersionUc;
    }


    @Override
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
