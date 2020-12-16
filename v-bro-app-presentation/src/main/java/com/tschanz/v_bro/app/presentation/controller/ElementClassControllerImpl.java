package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;


public class ElementClassControllerImpl implements ElementClassController {
    private final OpenElementClassUseCase openElementClassUc;


    public ElementClassControllerImpl(OpenElementClassUseCase openElementClassUc) {
        this.openElementClassUc = openElementClassUc;
    }


    @Override
    public void onElementClassSelected(String selectedElementClass) {
        if (selectedElementClass == null) {
            return;
        }

        var request = new OpenElementClassRequest(selectedElementClass, true);
        this.openElementClassUc.execute(request);
    }
}
