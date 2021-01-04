package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.usecase.select_dependency_denominations.SelectDependencyDenominationsUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class DependencyDenominationControllerImpl implements DependencyDenominationController {
    private final SelectDependencyDenominationsUseCase selectDependencyDenominationsUc;


    @Override
    public void onDenominationsSelected(List<DenominationItem> denominations) {
        if (denominations == null) {
            return;
        }

        var request = DenominationItem.toRequest(denominations);
        this.selectDependencyDenominationsUc.execute(request);
    }
}
