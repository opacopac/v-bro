package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MultiSelectableItemList;

import java.util.List;
import java.util.concurrent.Flow;


public interface ElementDenominationView {
    public void bindViewModel(
        Flow.Publisher<MultiSelectableItemList<DenominationItem>> denominationsList,
        ViewAction<List<DenominationItem>> selectDenominationsAction
    );
}
