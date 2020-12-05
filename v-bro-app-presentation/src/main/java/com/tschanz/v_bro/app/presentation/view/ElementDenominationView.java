package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.MultiSelectableItemList;
import com.tschanz.v_bro.app.presentation.actions.SelectDenominationsAction;
import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;

import java.util.concurrent.Flow;


public interface ElementDenominationView {
    public void bindViewModel(
        Flow.Publisher<MultiSelectableItemList<DenominationItem>> denominationsList,
        SelectDenominationsAction selectDenominationsAction
    );
}
