package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;


public interface DependencyListController {
    void onVersionFilterSelected(ElementVersionVector selectedDependencyVersion);
}
