package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionControllerImpl implements VersionController {
    private final OpenVersionUseCase openVersionUc;
    private final ProgressController progressController;


    @Override
    public void openVersion(String versionId) {
        if (versionId == null) {
            return;
        }

        this.progressController.startProgress();

        var request = new OpenVersionRequest(versionId, true);
        this.openVersionUc.execute(request);

        this.progressController.endProgress();
    }
}
