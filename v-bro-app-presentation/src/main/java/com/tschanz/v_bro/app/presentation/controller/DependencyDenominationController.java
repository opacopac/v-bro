package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;

import java.util.List;


public interface DependencyDenominationController {
    void selectDenominations(List<DenominationItem> denominations);
}
