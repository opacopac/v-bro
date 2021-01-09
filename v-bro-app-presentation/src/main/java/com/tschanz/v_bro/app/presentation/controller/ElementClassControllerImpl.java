package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ElementClassControllerImpl implements ElementClassController {
    private final OpenElementClassUseCase openElementClassUc;
    private final ProgressController progressController;


    @Override
    public void onElementClassSelected(String selectedElementClass) {
        if (selectedElementClass == null) {
            return;
        }

        this.progressController.startProgress();

        var request = new OpenElementClassRequest(selectedElementClass, true);
        this.openElementClassUc.execute(request);

        this.progressController.endProgress();
    }
}
