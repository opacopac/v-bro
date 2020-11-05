package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDependencyFilterAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDependencyVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionFilterAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DependencyFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_version.requestmodel.SelectDependencyVersionRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class DependencyListController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final SelectVersionFilterAction selectVersionFilterAction;
    private final SelectDependencyFilterAction selectDependencyFilterAction;
    private final SelectDependencyVersionUseCase selectDependencyVersionUc;


    public DependencyListController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        SelectVersionFilterAction selectVersionFilterAction,
        SelectDependencyFilterAction selectDependencyFilterAction,
        SelectDependencyVersionAction selectDependencyVersionAction,
        SelectDependencyVersionUseCase selectDependencyVersionUc
    ) {
        this.repoConnection = repoConnection;
        this.selectDependencyFilterAction = selectDependencyFilterAction;
        this.selectVersionFilterAction = selectVersionFilterAction;
        this.selectDependencyVersionUc = selectDependencyVersionUc;

        selectDependencyVersionAction.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));
    }


    private void onVersionFilterSelected(ElementVersionVector selectedDependencyVersion) {
        if (this.repoConnection.getCurrentValue() == null
            || selectedDependencyVersion == null
            || this.selectVersionFilterAction == null
            || this.selectDependencyFilterAction == null
        ) {
            return;
        }

        SelectDependencyVersionRequest request = new SelectDependencyVersionRequest(
            this.repoConnection.getCurrentValue().repoType,
            selectedDependencyVersion.getElementClass(),
            selectedDependencyVersion.getElementId(),
            selectedDependencyVersion.getVersionId(),
            VersionFilterItemConverter.toRequest(this.selectVersionFilterAction.getCurrentValue()),
            DependencyFilterItemConverter.toRequest(this.selectDependencyFilterAction.getCurrentValue())
        );
        this.selectDependencyVersionUc.execute(request);
    }
}
