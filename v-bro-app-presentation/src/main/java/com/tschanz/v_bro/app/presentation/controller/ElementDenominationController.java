package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;

import java.util.List;


public interface ElementDenominationController {
    void onDenominationsSelected(List<DenominationItem> denominations);
}
