package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionRequest;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyListControllerImpl implements DependencyListController {
    private final OpenDependencyVersionUseCase openDependencyVersionUc;
    private final ProgressController progressController;


    @Override
    public void selectDependencyVersion(ElementVersionVector selectedDependencyVersion) {
        if (selectedDependencyVersion == null) {
            return;
        }

        this.progressController.startProgress();

        var request = new OpenDependencyVersionRequest(
            selectedDependencyVersion.getElementClass(),
            selectedDependencyVersion.getElementId(),
            selectedDependencyVersion.getVersionId()
        );
        this.openDependencyVersionUc.execute(request);

        this.progressController.endProgress();
    }
}
