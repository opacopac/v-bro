package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.ElementController;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;

import java.util.concurrent.Flow;


public interface ElementView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementItem>> elementList,
        ElementController elementController
    );
}
