package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyElementClassControllerImpl implements DependencyElementClassController {
    private final SelectDependencyElementClassUseCase selectDependencyElementClassUc;


    @Override
    public void onElementClassSelected(String selectedElementClass) {
        if (selectedElementClass == null) {
            return;
        }

        var request = new SelectDependencyElementClassRequest(selectedElementClass);
        this.selectDependencyElementClassUc.execute(request);
    }
}
