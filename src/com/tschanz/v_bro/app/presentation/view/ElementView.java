package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface ElementView {
    void bindViewModel(
        Flow.Publisher<List<ElementItem>> elementList,
        SelectElementAction selectElementAction
    );
}
