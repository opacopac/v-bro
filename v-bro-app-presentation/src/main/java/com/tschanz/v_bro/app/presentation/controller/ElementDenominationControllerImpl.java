package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class ElementDenominationControllerImpl implements ElementDenominationController {
    private final SelectDenominationsUseCase selectElementDenominationUc;
    private final ProgressController progressController;


    @Override
    public void selectDenominations(List<DenominationItem> denominations) {
        if (denominations == null) {
            return;
        }

        this.progressController.startProgress();

        var request = DenominationItem.toRequest(denominations);
        this.selectElementDenominationUc.execute(request);

        this.progressController.endProgress();
    }
}
