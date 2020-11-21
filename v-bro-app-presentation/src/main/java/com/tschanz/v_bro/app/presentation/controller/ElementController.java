package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DependencyFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;


public class ElementController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<SelectedItemList<ElementClassItem>> elementClasses;
    private final BehaviorSubject<VersionFilterItem> versionFilter;
    private final BehaviorSubject<DependencyFilterItem> dependencyFilter;
    private final SelectElementUseCase selectElementUc;


    public ElementController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<SelectedItemList<ElementClassItem>> elementClasses,
        BehaviorSubject<VersionFilterItem> versionFilter,
        BehaviorSubject<DependencyFilterItem> dependencyFilter,
        SelectElementAction selectElementAction,
        SelectElementUseCase selectElementUc
    ) {
        this.repoConnection = repoConnection;
        this.elementClasses = elementClasses;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
        this.selectElementUc = selectElementUc;

        selectElementAction.subscribe(new GenericSubscriber<>(this::onElementSelected));
    }


    private void onElementSelected(String selectedElementId) {
        if (this.repoConnection.getCurrentValue() == null
            || this.elementClasses.getCurrentValue() == null
            || this.elementClasses.getCurrentValue().getSelectedItem() == null
            || selectedElementId == null
            || this.versionFilter.getCurrentValue() == null
            || this.dependencyFilter.getCurrentValue() == null
        ) {
            return;
        }

        SelectElementRequest request = new SelectElementRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.elementClasses.getCurrentValue().getSelectedItem().getId(),
            selectedElementId,
            VersionFilterItemConverter.toRequest(this.versionFilter.getCurrentValue()),
            DependencyFilterItemConverter.toRequest(this.dependencyFilter.getCurrentValue())
        );
        this.selectElementUc.execute(request);
    }
}
