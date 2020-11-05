package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionFilterAction;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class VersionFilterController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final SelectElementClassAction selectElementClassAction;
    private final SelectElementAction selectElementAction;
    private final SelectElementUseCase selectElementUc;


    public VersionFilterController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        SelectElementClassAction selectElementClassAction,
        SelectElementAction selectElementAction,
        SelectVersionFilterAction selectVersionFilterAction,
        SelectElementUseCase selectElementUc
    ) {
        this.repoConnection = repoConnection;
        this.selectElementAction = selectElementAction;
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementUc = selectElementUc;

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

        SelectElementRequest request = new SelectElementRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.selectElementClassAction.getCurrentValue(),
            this.selectElementAction.getCurrentValue(),
            this.getVersionFilterRequest(versionFilterItem)
        );
        this.selectElementUc.execute(request);
    }


    private VersionFilterRequest getVersionFilterRequest(VersionFilterItem versionFilter) {
        return new VersionFilterRequest(
            versionFilter.getMinGueltigVon(),
            versionFilter.getMaxGueltigBis(),
            versionFilter.getMinPflegestatus()
        );
    }
}
