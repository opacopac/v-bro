package com.tschanz.v_bro.element_classes.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.DenominationItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface ElementDenominationView {
    void bindDenominationList(Flow.Publisher<List<DenominationItem>> denominationList);

    void bindSelectDenominationsAction(BehaviorSubject<List<DenominationItem>> selectDenominationsAction);
}
