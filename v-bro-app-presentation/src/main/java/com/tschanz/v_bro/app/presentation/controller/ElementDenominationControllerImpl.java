package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsRequest;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;

import java.util.List;


public class ElementDenominationControllerImpl implements ElementDenominationController {
    private final SelectDenominationsUseCase selectElementDenominationUc;


    public ElementDenominationControllerImpl(SelectDenominationsUseCase selectElementDenominationUc) {
        this.selectElementDenominationUc = selectElementDenominationUc;
    }


    @Override
    public void onDenominationsSelected(List<DenominationItem> denominations) {
        if (denominations == null) {
            return;
        }

        var request = new SelectDenominationsRequest(DenominationItem.toRequest(denominations));
        this.selectElementDenominationUc.execute(request);
    }
}
