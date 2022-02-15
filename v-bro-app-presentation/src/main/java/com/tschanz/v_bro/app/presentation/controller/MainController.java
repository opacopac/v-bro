package com.tschanz.v_bro.app.presentation.controller;


public interface MainController {
    ConnectionController getConnectionController();

    ElementClassController getElementClassController();

    ElementDenominationController getElementDenominationController();

    RefreshController getRefreshController();

    ElementController getElementController();

    VersionFilterController getVersionFilterController();

    VersionController getVersionController();

    DependencyDirectionController getDependencyDirectionController();

    DependencyElementClassController getDependencyElementClassController();

    DependencyDenominationController getDependencyDenominationController();

    DependencyElementFilterController getDependencyElementFilterController();

    DependencyListController getDependencyListController();

    VersionAggregateHistoryController getVersionAggregateHistoryController();
}
