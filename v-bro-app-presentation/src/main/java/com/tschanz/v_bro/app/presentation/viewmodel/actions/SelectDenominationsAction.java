package com.tschanz.v_bro.app.presentation.viewmodel.actions;

import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;

import java.util.List;


public class SelectDenominationsAction extends ViewAction<List<DenominationItem>> {
    public SelectDenominationsAction(List<DenominationItem> initialDenominations) {
        super(initialDenominations);
    }
}
