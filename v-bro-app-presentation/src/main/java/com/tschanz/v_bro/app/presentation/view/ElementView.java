package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.presentation.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;

import java.util.concurrent.Flow;


public interface ElementView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementItem>> elementList,
        SelectElementAction selectElementAction
    );
}
