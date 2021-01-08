package com.tschanz.v_bro.app.presentation.controller;


public interface MainController {
    ConnectionController getConnectionController();

    ElementClassController getElementClassController();

    ElementDenominationController getElementDenominationController();

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
