package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class ElementDenominationControllerImpl implements ElementDenominationController {
    private final SelectDenominationsUseCase selectElementDenominationUc;


    @Override
    public void onDenominationsSelected(List<DenominationItem> denominations) {
        if (denominations == null) {
            return;
        }

        var request = DenominationItem.toRequest(denominations);
        this.selectElementDenominationUc.execute(request);
    }
}
