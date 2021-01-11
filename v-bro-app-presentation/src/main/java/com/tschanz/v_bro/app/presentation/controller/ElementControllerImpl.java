package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
public class ElementControllerImpl implements ElementController {
    private final QueryElementsUseCase queryElementsUc;
    private final OpenElementUseCase openElementUc;
    private final ProgressController progressController;


    @Override
    public List<ElementItem> queryElement(String queryText) {
        if (queryText == null) {
            return Collections.emptyList();
        }

        this.progressController.startProgress();

        var request = new QueryElementsRequest(queryText);
        var response = this.queryElementsUc.execute(request);

        this.progressController.endProgress();

        return ElementItem.fromResponse(response);
    }


    @Override
    public void openElement(String selectedElementId) {
        if (selectedElementId == null) {
            return;
        }

        this.progressController.startProgress();

        var request = new OpenElementRequest(selectedElementId, true,true);
        this.openElementUc.execute(request);

        this.progressController.endProgress();
    }
}
