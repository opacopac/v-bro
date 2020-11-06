package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.SelectedItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;

import java.util.concurrent.Flow;


public interface ElementClassView {
    void bindViewModel(
        Flow.Publisher<SelectedItemList<ElementClassItem>> elementClassList,
        SelectElementClassAction selectElementClassAction
    );
}
