package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class ElementClassController {
    private final OpenElementClassUseCase openElementClassUc;


    public ElementClassController(
        ViewAction<String> selectElementClassAction,
        OpenElementClassUseCase openElementClassUc
    ) {
        this.openElementClassUc = openElementClassUc;
        selectElementClassAction.subscribe(new GenericSubscriber<>(this::onElementClassSelected));
    }


    public void onElementClassSelected(String selectedElementClass) {
        if (selectedElementClass == null) {
            return;
        }

        var request = new OpenElementClassRequest(selectedElementClass);
        this.openElementClassUc.execute(request);
    }
}
