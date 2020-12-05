package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.actions.SelectVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DependencyFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.usecase.select_version.requestmodel.SelectVersionRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCase;


public class VersionController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses;
    private final BehaviorSubject<SelectableItemList<ElementItem>> elements;
    private final BehaviorSubject<VersionFilterItem> versionFilter;
    private final BehaviorSubject<DependencyFilterItem> dependencyFilter;
    private final SelectVersionUseCase selectVersionUc;


    public VersionController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses,
        BehaviorSubject<SelectableItemList<ElementItem>> elements,
        BehaviorSubject<VersionFilterItem> versionFilter,
        BehaviorSubject<DependencyFilterItem> dependencyFilter,
        SelectVersionAction selectVersionAction,
        SelectVersionUseCase selectVersionUc
    ) {
        this.repoConnection = repoConnection;
        this.elementClasses = elementClasses;
        this.elements = elements;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
        this.selectVersionUc = selectVersionUc;

        selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));
    }


    public void onVersionSelected(String versionId) {
        if (this.repoConnection.getCurrentValue() == null
            || this.elementClasses.getCurrentValue() == null
            || this.elementClasses.getCurrentValue().getSelectedItem() == null
            || this.elements.getCurrentValue() == null
            || this.elements.getCurrentValue().getSelectedItem() == null
            || versionId == null
            || this.versionFilter.getCurrentValue() == null
            || this.dependencyFilter.getCurrentValue() == null
        ) {
            return;
        }

        SelectVersionRequest request = new SelectVersionRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.elementClasses.getCurrentValue().getSelectedItem().getId(),
            this.elements.getCurrentValue().getSelectedItem().getId(),
            versionId,
            VersionFilterItemConverter.toRequest(this.versionFilter.getCurrentValue()),
            DependencyFilterItemConverter.toRequest(this.dependencyFilter.getCurrentValue())
        );
        this.selectVersionUc.execute(request);
    }
}
