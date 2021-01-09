package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionFilterControllerImpl implements VersionFilterController {
    private final SelectVersionFilterUseCase selectVersionFilterUc;
    private final ProgressController progressController;


    public void onVersionFilterSelected(VersionFilterItem versionFilterItem) {
        if (versionFilterItem == null) {
            return;
        }

        this.progressController.startProgress();

        var request = new SelectVersionFilterRequest(versionFilterItem.getMinGueltigVon(), versionFilterItem.getMaxGueltigBis(), versionFilterItem.getMinPflegestatus());
        this.selectVersionFilterUc.execute(request);

        this.progressController.endProgress();
    }
}
