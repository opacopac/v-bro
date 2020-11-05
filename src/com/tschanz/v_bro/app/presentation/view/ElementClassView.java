package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface ElementClassView {
    void bindViewModel(
        Flow.Publisher<List<ElementClassItem>> elementClassList,
        SelectElementClassAction selectElementClassAction
    );
}
