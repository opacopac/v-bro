package com.tschanz.v_bro.element_classes.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.ElementClassItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface ElementClassView {
    void bindElementClassList(Flow.Publisher<List<ElementClassItem>> elementClassList);

    void bindSelectElementClassAction(BehaviorSubject<ElementClassItem> selectElementClassAction);
}
