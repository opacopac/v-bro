package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionControllerImpl implements VersionController {
    private final OpenVersionUseCase openVersionUc;


    @Override
    public void onVersionSelected(String versionId) {
        if (versionId == null) {
            return;
        }

        var request = new OpenVersionRequest(versionId);
        this.openVersionUc.execute(request);
    }
}
