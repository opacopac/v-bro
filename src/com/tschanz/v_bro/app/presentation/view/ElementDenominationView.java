package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDenominationsAction;
import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface ElementDenominationView {
    public void bindViewModel(
        Flow.Publisher<List<DenominationItem>> denominationsList,
        SelectDenominationsAction selectDenominationsAction
    );
}
