package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.SelectedItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;

import java.util.concurrent.Flow;


public interface ElementView {
    void bindViewModel(
        Flow.Publisher<SelectedItemList<ElementItem>> elementList,
        SelectElementAction selectElementAction
    );
}
