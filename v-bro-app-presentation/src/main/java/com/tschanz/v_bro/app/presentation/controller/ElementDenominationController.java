package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DenominationItemConverter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationUseCase;
import com.tschanz.v_bro.app.usecase.select_element_denomination.requestmodel.SelectElementDenominationRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;

import java.util.List;


public class ElementDenominationController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses;
    private final SelectElementDenominationUseCase selectElementDenominationUc;


    public ElementDenominationController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses,
        ViewAction<List<DenominationItem>> selectDenominationsAction,
        SelectElementDenominationUseCase selectElementDenominationUc
    ) {
        this.repoConnection = repoConnection;
        this.elementClasses = elementClasses;
        this.selectElementDenominationUc = selectElementDenominationUc;

        selectDenominationsAction.subscribe(new GenericSubscriber<>(this::onDenominationsSelected));
    }


    public void onDenominationsSelected(List<DenominationItem> denominations) {
        if (this.repoConnection.getCurrentValue() == null
            || this.elementClasses.getCurrentValue() == null
            || this.elementClasses.getCurrentValue().getSelectedItem() == null
            || denominations == null
        ) {
            return;
        }

        SelectElementDenominationRequest request = new SelectElementDenominationRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.elementClasses.getCurrentValue().getSelectedItem().getId(),
            DenominationItemConverter.toRequest(denominations)
        );
        this.selectElementDenominationUc.execute(request);
    }
}
