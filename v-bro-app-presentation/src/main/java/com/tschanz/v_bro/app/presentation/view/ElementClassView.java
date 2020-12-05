package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.presentation.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;

import java.util.concurrent.Flow;


public interface ElementClassView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementClassItem>> elementClassList,
        SelectElementClassAction selectElementClassAction
    );
}
