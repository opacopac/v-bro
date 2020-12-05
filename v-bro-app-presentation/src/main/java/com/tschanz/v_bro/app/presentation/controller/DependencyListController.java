package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.DependencyFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.actions.SelectDependencyVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DependencyFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_version.requestmodel.SelectDependencyVersionRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class DependencyListController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<VersionFilterItem> versionFilter;
    private final BehaviorSubject<DependencyFilterItem> dependencyFilter;
    private final SelectDependencyVersionUseCase selectDependencyVersionUc;


    public DependencyListController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<VersionFilterItem> versionFilter,
        BehaviorSubject<DependencyFilterItem> dependencyFilter,
        SelectDependencyVersionAction selectDependencyVersionAction,
        SelectDependencyVersionUseCase selectDependencyVersionUc
    ) {
        this.repoConnection = repoConnection;
        this.dependencyFilter = dependencyFilter;
        this.versionFilter = versionFilter;
        this.selectDependencyVersionUc = selectDependencyVersionUc;

        selectDependencyVersionAction.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));
    }


    public void onVersionFilterSelected(ElementVersionVector selectedDependencyVersion) {
        if (this.repoConnection.getCurrentValue() == null
            || selectedDependencyVersion == null
            || this.versionFilter == null
            || this.dependencyFilter == null
        ) {
            return;
        }

        SelectDependencyVersionRequest request = new SelectDependencyVersionRequest(
            this.repoConnection.getCurrentValue().repoType,
            selectedDependencyVersion.getElementClass(),
            selectedDependencyVersion.getElementId(),
            selectedDependencyVersion.getVersionId(),
            VersionFilterItemConverter.toRequest(this.versionFilter.getCurrentValue()),
            DependencyFilterItemConverter.toRequest(this.dependencyFilter.getCurrentValue())
        );
        this.selectDependencyVersionUc.execute(request);
    }
}
