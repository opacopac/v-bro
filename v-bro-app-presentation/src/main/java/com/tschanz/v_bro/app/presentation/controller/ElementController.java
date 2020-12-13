package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.element.QueryElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;


public class ElementController {
    private final QueryElementsUseCase queryElementsUc;
    private final OpenElementUseCase openElementUc;


    public ElementController(
        ViewAction<QueryElementItem> queryElementAction,
        ViewAction<String> selectElementAction,
        QueryElementsUseCase queryElementsUc,
        OpenElementUseCase openElementUc
    ) {
        this.queryElementsUc = queryElementsUc;
        this.openElementUc = openElementUc;

        queryElementAction.subscribe(new GenericSubscriber<>(this::onQueryElement));
        selectElementAction.subscribe(new GenericSubscriber<>(this::onElementSelected));
    }


    public void onQueryElement(QueryElementItem queryElementItem) {
        if (queryElementItem == null) {
            return;
        }

        var request = new QueryElementsRequest(queryElementItem.getQueryText(), false);
        this.queryElementsUc.execute(request);
    }


    public void onElementSelected(String selectedElementId) {
        if (selectedElementId == null) {
            return;
        }

        var request = new OpenElementRequest(selectedElementId);
        this.openElementUc.execute(request);
    }
}
