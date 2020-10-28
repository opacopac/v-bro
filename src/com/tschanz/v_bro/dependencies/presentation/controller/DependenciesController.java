package com.tschanz.v_bro.dependencies.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.StatusItem;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.dependencies.presentation.view.DependenciesView;
import com.tschanz.v_bro.dependencies.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies.ReadFwdDependenciesResponse;
import com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies.ReadFwdDependenciesUseCase;
import com.tschanz.v_bro.element_classes.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.elements.presentation.viewmodel.ElementItem;
import com.tschanz.v_bro.repo.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.versions.presentation.viewmodel.VersionItem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class DependenciesController {
    private final DependenciesView dependenciesView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final BehaviorSubject<ElementItem> selectElementAction;
    private final BehaviorSubject<VersionItem> selectVersionAction;
    private final BehaviorSubject<List<FwdDependencyItem>> fwdDependencies = new BehaviorSubject<>(Collections.emptyList());
    private final ReadFwdDependenciesUseCase readFwdDependenciesUc;


    public DependenciesController(
        DependenciesView dependenciesView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        BehaviorSubject<ElementItem> selectElementAction,
        BehaviorSubject<VersionItem> selectVersionAction,
        ReadFwdDependenciesUseCase readFwdDependenciesUc
    ) {
        this.dependenciesView = dependenciesView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementAction = selectElementAction;
        this.selectVersionAction = selectVersionAction;
        this.readFwdDependenciesUc = readFwdDependenciesUc;

        this.selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));

        this.dependenciesView.bindFwdDependencyList(this.fwdDependencies);
    }


    private void onVersionSelected(VersionItem selectedVersion) {
        if (selectedVersion == null) {
            return;
        }

        try {
            this.updateFwdDependencies(selectedVersion);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void updateFwdDependencies(VersionItem selectedVersion) throws VBroAppException {
        ReadFwdDependenciesResponse response = this.readFwdDependenciesUc.readFwdDependencies(
            new OpenConnectionResponse.RepoConnection(this.repoConnection.getCurrentValue().getRepoType()),
            this.selectElementClassAction.getCurrentValue().getName(),
            this.selectElementAction.getCurrentValue().getId(),
            selectedVersion.getId()
        );

        List<FwdDependencyItem> fwdDependencyList = response.fwdDependencies
            .stream()
            .map(dep -> new FwdDependencyItem(
                dep.elementClass,
                dep.elementId,
                dep.versions
                    .stream()
                    .map(ver -> new VersionItem(ver.getId(), ver.getGueltigVon(), ver.getGueltigBis()))
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());

        this.fwdDependencies.next(fwdDependencyList);
    }
}
