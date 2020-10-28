package com.tschanz.v_bro.elements.presentation.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.elements.presentation.viewmodel.ElementItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface ElementView {
    void bindElementList(Flow.Publisher<List<ElementItem>> elementList);

    void bindSelectElementAction(BehaviorSubject<ElementItem> selectElementAction);
}