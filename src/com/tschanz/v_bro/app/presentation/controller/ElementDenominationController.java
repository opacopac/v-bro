package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.select_element_denomination.requestmodel.SelectElementDenominationRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationUseCase;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;

import java.util.List;
import java.util.stream.Collectors;


public class ElementDenominationController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final SelectElementDenominationUseCase selectElementDenominationUc;


    public ElementDenominationController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        BehaviorSubject<List<DenominationItem>> selectDenominationsAction,
        SelectElementDenominationUseCase selectElementDenominationUc
    ) {
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementDenominationUc = selectElementDenominationUc;

        selectDenominationsAction.subscribe(new GenericSubscriber<>(this::onDenominationsSelected));
    }


    private void onDenominationsSelected(List<DenominationItem> denominations) {
        if (this.repoConnection.getCurrentValue() == null
            || this.selectElementClassAction.getCurrentValue() == null
            || denominations == null
        ) {
            return;
        }

        RepoConnectionItem repoConnection = this.repoConnection.getCurrentValue();
        String elementClass = this.selectElementClassAction.getCurrentValue().getName();
        List<String> denominationNames = denominations
            .stream()
            .map(DenominationItem::getName)
            .collect(Collectors.toList());
        SelectElementDenominationRequest request = new SelectElementDenominationRequest(repoConnection.repoType, elementClass, denominationNames);
        this.selectElementDenominationUc.execute(request);
    }
}
