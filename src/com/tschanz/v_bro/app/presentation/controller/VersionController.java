package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version.requestmodel.SelectVersionRequest;
import com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCase;


public class VersionController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final BehaviorSubject<ElementItem> selectElementAction;
    private final BehaviorSubject<VersionFilterItem> effectiveVersionFilter;
    private final BehaviorSubject<DependencyFilterItem> dependencyFilter;
    private final SelectVersionUseCase selectVersionUc;


    public VersionController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        BehaviorSubject<ElementItem> selectElementAction,
        BehaviorSubject<VersionItem> selectVersionAction,
        BehaviorSubject<VersionFilterItem> effectiveVersionFilter,
        BehaviorSubject<DependencyFilterItem> dependencyFilter,
        SelectVersionUseCase selectVersionUc
    ) {
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementAction = selectElementAction;
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.dependencyFilter = dependencyFilter;
        this.selectVersionUc = selectVersionUc;

        selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));
    }


    private void onVersionSelected(VersionItem versionItem) {
        if (this.repoConnection.getCurrentValue() == null
            || this.selectElementClassAction.getCurrentValue() == null
            || this.selectElementAction == null
            || versionItem == null
            || this.effectiveVersionFilter.getCurrentValue() == null
            || this.dependencyFilter.getCurrentValue() == null
        ) {
            return;
        }

        SelectVersionRequest request = new SelectVersionRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.selectElementClassAction.getCurrentValue().getName(),
            this.selectElementAction.getCurrentValue().getId(),
            versionItem.getId(),
            this.getVersionFilterRequest(this.effectiveVersionFilter.getCurrentValue()),
            new DependencyFilterRequest(this.dependencyFilter.getCurrentValue().isFwd)
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
