package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDependencyFilterAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionAction;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version.requestmodel.SelectVersionRequest;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCase;


public class VersionController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final SelectElementClassAction selectElementClassAction;
    private final SelectElementAction selectElementAction;
    private final BehaviorSubject<VersionFilterItem> effectiveVersionFilter;
    private final SelectDependencyFilterAction selectDependencyFilterAction;
    private final SelectVersionUseCase selectVersionUc;


    public VersionController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        SelectElementClassAction selectElementClassAction,
        SelectElementAction selectElementAction,
        SelectVersionAction selectVersionAction,
        BehaviorSubject<VersionFilterItem> effectiveVersionFilter,
        SelectDependencyFilterAction selectDependencyFilterAction,
        SelectVersionUseCase selectVersionUc
    ) {
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementAction = selectElementAction;
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.selectDependencyFilterAction = selectDependencyFilterAction;
        this.selectVersionUc = selectVersionUc;

        selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));
    }


    private void onVersionSelected(String versionId) {
        if (this.repoConnection.getCurrentValue() == null
            || this.selectElementClassAction.getCurrentValue() == null
            || this.selectElementAction == null
            || versionId == null
            || this.effectiveVersionFilter.getCurrentValue() == null
            || this.selectDependencyFilterAction.getCurrentValue() == null
        ) {
            return;
        }

        SelectVersionRequest request = new SelectVersionRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.selectElementClassAction.getCurrentValue(),
            this.selectElementAction.getCurrentValue(),
            versionId,
            this.getVersionFilterRequest(this.effectiveVersionFilter.getCurrentValue()),
            new DependencyFilterRequest(this.selectDependencyFilterAction.getCurrentValue().isFwd)
        );
        this.selectVersionUc.execute(request);
    }


    private VersionFilterRequest getVersionFilterRequest(VersionFilterItem versionFilter) {
        return new VersionFilterRequest(
            versionFilter.getMinGueltigVon(),
            versionFilter.getMaxGueltigBis(),
            versionFilter.getMinPflegestatus()
        );
    }
}
