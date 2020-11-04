package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCase;
import com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel.SelectVersionFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class VersionFilterController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final BehaviorSubject<ElementItem> selectElementAction;
    private final SelectVersionFilterUseCase selectVersionFilterUc;


    public VersionFilterController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        BehaviorSubject<ElementItem> selectElementAction,
        BehaviorSubject<VersionFilterItem> selectVersionFilterAction,
        SelectVersionFilterUseCase selectVersionFilterUc
    ) {
        this.repoConnection = repoConnection;
        this.selectElementAction = selectElementAction;
        this.selectElementClassAction = selectElementClassAction;
        this.selectVersionFilterUc = selectVersionFilterUc;

        selectVersionFilterAction.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));
    }


    private void onVersionFilterSelected(VersionFilterItem versionFilterItem) {
        if (this.repoConnection.getCurrentValue() == null
            || this.selectElementClassAction.getCurrentValue() == null
            || this.selectElementAction == null
            || versionFilterItem == null
        ) {
            return;
        }

        SelectVersionFilterRequest request = new SelectVersionFilterRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.selectElementClassAction.getCurrentValue().getName(),
            this.selectElementAction.getCurrentValue().getId(),
            this.getVersionFilterRequest(versionFilterItem)
        );
        this.selectVersionFilterUc.execute(request);
    }


    private VersionFilterRequest getVersionFilterRequest(VersionFilterItem versionFilter) {
        return new VersionFilterRequest(
            versionFilter.getMinGueltigVon(),
            versionFilter.getMaxGueltigBis(),
            versionFilter.getMinPflegestatus()
        );
    }
}
