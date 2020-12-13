package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.element.QueryElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;

import java.util.concurrent.Flow;


public interface ElementView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementItem>> elementList,
        ViewAction<String> selectElementAction,
        ViewAction<QueryElementItem> queryElementAction
    );
}
