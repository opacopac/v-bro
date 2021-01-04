package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyElementClassController;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;

import java.util.concurrent.Flow;


public interface DependencyElementClassView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementClassItem>> elementClassList,
        DependencyElementClassController dependencyElementClassController
    );
}
