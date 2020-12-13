package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class VersionController {
    private final OpenVersionUseCase openVersionUc;


    public VersionController(
        ViewAction<String> selectVersionAction,
        OpenVersionUseCase openVersionUc
    ) {
        this.openVersionUc = openVersionUc;

        selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));
    }


    public void onVersionSelected(String versionId) {
        if (versionId == null) {
            return;
        }

        var request = new OpenVersionRequest(versionId);
        this.openVersionUc.execute(request);
    }
}
