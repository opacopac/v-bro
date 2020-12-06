package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DependencyFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.usecase.query_element.QueryElementUseCase;
import com.tschanz.v_bro.app.usecase.query_element.requestmodel.QueryElementRequest;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;

import java.util.stream.Collectors;


public class ElementController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses;
    private final BehaviorSubject<MultiSelectableItemList<DenominationItem>> denominations;
    private final BehaviorSubject<VersionFilterItem> versionFilter;
    private final BehaviorSubject<DependencyFilterItem> dependencyFilter;
    private final QueryElementUseCase queryElementUc;
    private final SelectElementUseCase selectElementUc;


    public ElementController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses,
        BehaviorSubject<MultiSelectableItemList<DenominationItem>> denominations,
        BehaviorSubject<VersionFilterItem> versionFilter,
        BehaviorSubject<DependencyFilterItem> dependencyFilter,
        ViewAction<QueryElementItem> queryElementAction,
        ViewAction<String> selectElementAction,
        QueryElementUseCase queryElementUc,
        SelectElementUseCase selectElementUc
    ) {
        this.repoConnection = repoConnection;
        this.elementClasses = elementClasses;
        this.denominations = denominations;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
        this.queryElementUc = queryElementUc;
        this.selectElementUc = selectElementUc;

        queryElementAction.subscribe(new GenericSubscriber<>(this::onQueryElement));
        selectElementAction.subscribe(new GenericSubscriber<>(this::onElementSelected));
    }


    public void onQueryElement(QueryElementItem queryElementItem) {
        if (this.repoConnection.getCurrentValue() == null
            || this.elementClasses.getCurrentValue() == null
            || this.denominations.getCurrentValue() == null
            || queryElementItem == null
        ) {
            return;
        }

        var request = new QueryElementRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.elementClasses.getCurrentValue().getSelectedItem().getName(),
            this.denominations.getCurrentValue().getSelectedItems().stream().map(DenominationItem::getName).collect(Collectors.toList()),
            queryElementItem.getQueryText(),
            queryElementItem.getSchedulingTimestamp()
        );
        this.queryElementUc.execute(request);
    }


    public void onElementSelected(String selectedElementId) {
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
