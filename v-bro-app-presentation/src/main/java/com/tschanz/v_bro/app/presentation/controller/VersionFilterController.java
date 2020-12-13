package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCase;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class VersionFilterController {
    private final SelectVersionFilterUseCase selectVersionFilterUc;


    public VersionFilterController(
        ViewAction<VersionFilterItem> selectVersionFilterAction,
        SelectVersionFilterUseCase selectVersionFilterUc
    ) {
        this.selectVersionFilterUc = selectVersionFilterUc;

        selectVersionFilterAction.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));
    }


    public void onVersionFilterSelected(VersionFilterItem versionFilterItem) {
        if (versionFilterItem == null) {
            return;
        }

        var request = new SelectVersionFilterRequest(versionFilterItem.getMinGueltigVon(), versionFilterItem.getMaxGueltigBis(), versionFilterItem.getMinPflegestatus());
        this.selectVersionFilterUc.execute(request);
    }
}
