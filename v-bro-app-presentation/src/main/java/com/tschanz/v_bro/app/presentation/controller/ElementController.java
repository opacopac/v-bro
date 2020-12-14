package com.tschanz.v_bro.app.presentation.controller;


import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;

import java.util.List;

public interface ElementController {
    List<ElementItem> onQueryElement(String queryText);

    void onElementSelected(String selectedElementId);
}
