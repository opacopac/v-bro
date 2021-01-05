package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ElementClassControllerImpl implements ElementClassController {
    private final OpenElementClassUseCase openElementClassUc;


    @Override
    public void onElementClassSelected(String selectedElementClass) {
        if (selectedElementClass == null) {
            return;
        }

        var request = new OpenElementClassRequest(selectedElementClass, true, false);
        this.openElementClassUc.execute(request);
    }
}
