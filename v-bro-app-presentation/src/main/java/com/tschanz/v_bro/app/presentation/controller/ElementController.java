package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.element.QueryElementItem;


public interface ElementController {
    void onQueryElement(QueryElementItem queryElementItem);

    void onElementSelected(String selectedElementId);
}
