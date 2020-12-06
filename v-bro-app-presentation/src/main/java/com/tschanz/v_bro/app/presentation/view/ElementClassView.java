package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;

import java.util.concurrent.Flow;


public interface ElementClassView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementClassItem>> elementClassList,
        ViewAction<String> selectElementClassAction
    );
}
