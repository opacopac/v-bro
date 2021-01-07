package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.select_dependency_element_filter.SelectDependencyElementFilterRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_element_filter.SelectDependencyElementFilterUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyElementFilterControllerImpl implements DependencyElementFilterController {
    private final SelectDependencyElementFilterUseCase selectDependencyElementFilterUc;


    public void onDependencyElementQuerySelected(String query) {
        if (query == null) {
            return;
        }

        var request = new SelectDependencyElementFilterRequest(query);
        this.selectDependencyElementFilterUc.execute(request);
    }
}
