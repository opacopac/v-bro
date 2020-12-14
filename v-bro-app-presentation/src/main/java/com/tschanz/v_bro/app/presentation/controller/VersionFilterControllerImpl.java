package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCase;


public class VersionFilterControllerImpl implements VersionFilterController {
    private final SelectVersionFilterUseCase selectVersionFilterUc;


    public VersionFilterControllerImpl(SelectVersionFilterUseCase selectVersionFilterUc) {
        this.selectVersionFilterUc = selectVersionFilterUc;
    }


    public void onVersionFilterSelected(VersionFilterItem versionFilterItem) {
        if (versionFilterItem == null) {
            return;
        }

        var request = new SelectVersionFilterRequest(versionFilterItem.getMinGueltigVon(), versionFilterItem.getMaxGueltigBis(), versionFilterItem.getMinPflegestatus());
        this.selectVersionFilterUc.execute(request);
    }
}
