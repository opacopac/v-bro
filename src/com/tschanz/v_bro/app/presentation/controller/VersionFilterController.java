package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionFilterAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DependencyFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class VersionFilterController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<SelectedItemList<ElementClassItem>> elementClasses;
    private final BehaviorSubject<SelectedItemList<ElementItem>> elements;
    private final BehaviorSubject<DependencyFilterItem> dependencyFilter;
    private final SelectElementUseCase selectElementUc;


    public VersionFilterController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<SelectedItemList<ElementClassItem>> elementClasses,
        BehaviorSubject<SelectedItemList<ElementItem>> elements,
        BehaviorSubject<DependencyFilterItem> dependencyFilter,
        SelectVersionFilterAction selectVersionFilterAction,
        SelectElementUseCase selectElementUc
    ) {
        this.repoConnection = repoConnection;
        this.elementClasses = elementClasses;
        this.elements = elements;
        this.dependencyFilter = dependencyFilter;
        this.selectElementUc = selectElementUc;

        selectVersionFilterAction.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));
    }


    private void onVersionFilterSelected(VersionFilterItem versionFilterItem) {
        if (this.repoConnection.getCurrentValue() == null
            || this.elementClasses.getCurrentValue() == null
            || this.elementClasses.getCurrentValue().getSelectedItem() == null
            || this.elements.getCurrentValue() == null
            || this.elements.getCurrentValue().getSelectedItem() == null
            || versionFilterItem == null
            || this.dependencyFilter.getCurrentValue() == null
        ) {
            return;
        }

        SelectElementRequest request = new SelectElementRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.elementClasses.getCurrentValue().getSelectedItem().getId(),
            this.elements.getCurrentValue().getSelectedItem().getId(),
            VersionFilterItemConverter.toRequest(versionFilterItem),
            DependencyFilterItemConverter.toRequest(this.dependencyFilter.getCurrentValue())
        );
        this.selectElementUc.execute(request);
    }
}
