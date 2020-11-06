package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.DependencyFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DependencyFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.usecase.select_element_class.requestmodel.SelectElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassUseCase;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;


public class ElementClassController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<VersionFilterItem> versionFilter;
    private final BehaviorSubject<DependencyFilterItem> dependencyFilter;
    private final SelectElementClassUseCase selectElementClassUc;


    public ElementClassController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<VersionFilterItem> versionFilter,
        BehaviorSubject<DependencyFilterItem> dependencyFilter,
        SelectElementClassAction selectElementClassAction,
        SelectElementClassUseCase selectElementClassUc
    ) {
        this.repoConnection = repoConnection;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
        this.selectElementClassUc = selectElementClassUc;
        selectElementClassAction.subscribe(new GenericSubscriber<>(this::onElementClassSelected));
    }


    private void onElementClassSelected(String selectedElementClass) {
        if (this.repoConnection.getCurrentValue() == null
            || selectedElementClass == null
            || this.versionFilter.getCurrentValue() == null
            || this.dependencyFilter.getCurrentValue() == null
        ) {
            return;
        }

        SelectElementClassRequest request = new SelectElementClassRequest(
            this.repoConnection.getCurrentValue().repoType,
            selectedElementClass,
            VersionFilterItemConverter.toRequest(this.versionFilter.getCurrentValue()),
            DependencyFilterItemConverter.toRequest(this.dependencyFilter.getCurrentValue())
        );
        this.selectElementClassUc.execute(request);
    }
}
