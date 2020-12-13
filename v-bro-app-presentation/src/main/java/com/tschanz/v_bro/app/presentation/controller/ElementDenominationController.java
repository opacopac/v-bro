package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsRequest;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;

import java.util.List;


public class ElementDenominationController {
    private final SelectDenominationsUseCase selectElementDenominationUc;


    public ElementDenominationController(
        ViewAction<List<DenominationItem>> selectDenominationsAction,
        SelectDenominationsUseCase selectElementDenominationUc
    ) {
        this.selectElementDenominationUc = selectElementDenominationUc;

        selectDenominationsAction.subscribe(new GenericSubscriber<>(this::onDenominationsSelected));
    }


    public void onDenominationsSelected(List<DenominationItem> denominations) {
        if (denominations == null) {
            return;
        }

        var request = new SelectDenominationsRequest(DenominationItem.toRequest(denominations));
        this.selectElementDenominationUc.execute(request);
    }
}
