package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;

import java.util.Collections;
import java.util.List;


public class ElementControllerImpl implements ElementController {
    private final QueryElementsUseCase queryElementsUc;
    private final OpenElementUseCase openElementUc;


    public ElementControllerImpl(
        QueryElementsUseCase queryElementsUc,
        OpenElementUseCase openElementUc
    ) {
        this.queryElementsUc = queryElementsUc;
        this.openElementUc = openElementUc;
    }


    @Override
    public List<ElementItem> onQueryElement(String queryText) {
        if (queryText == null) {
            return Collections.emptyList();
        }

        var request = new QueryElementsRequest(queryText);
        var response = this.queryElementsUc.execute(request);

        return ElementItem.fromResponse(response);
    }


    @Override
    public void onElementSelected(String selectedElementId) {
        if (selectedElementId == null) {
            return;
        }

        var request = new OpenElementRequest(selectedElementId, true);
        this.openElementUc.execute(request);
    }
}
