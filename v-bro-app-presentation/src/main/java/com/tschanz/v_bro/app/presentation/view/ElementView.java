package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.ElementController;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;


public interface ElementView {
    void bindViewModel(
        BehaviorSubject<ElementItem> currentElement,
        ElementController elementController
    );
}
