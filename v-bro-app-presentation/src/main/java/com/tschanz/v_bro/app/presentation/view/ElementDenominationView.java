package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.ElementDenominationController;
import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;

import java.util.concurrent.Flow;


public interface ElementDenominationView {
    void bindViewModel(
        Flow.Publisher<MultiSelectableItemList<DenominationItem>> denominationsList,
        ElementDenominationController elementDenominationController
    );
}
