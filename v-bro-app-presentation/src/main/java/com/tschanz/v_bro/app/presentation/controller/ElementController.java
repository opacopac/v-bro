package com.tschanz.v_bro.app.presentation.controller;


import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;

import java.util.List;

public interface ElementController {
    List<ElementItem> queryElement(String queryText);

    void openElement(String selectedElementId);
}
