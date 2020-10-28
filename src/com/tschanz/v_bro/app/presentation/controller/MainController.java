package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.view.MainView;
import com.tschanz.v_bro.dependencies.presentation.controller.DependenciesController;
import com.tschanz.v_bro.element_classes.presentation.controller.ElementClassController;
import com.tschanz.v_bro.element_classes.presentation.controller.ElementDenominationController;
import com.tschanz.v_bro.elements.presentation.controller.ElementController;
import com.tschanz.v_bro.element_classes.usecase.read_element_denominations.ReadElementDenominationsUseCase;
import com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies.ReadFwdDependenciesUseCase;
import com.tschanz.v_bro.version_aggregates.presentation.controller.VersionAggregateController;
import com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate.ReadVersionAggregateUseCase;
import com.tschanz.v_bro.versions.presentation.controller.VersionFilterController;
import com.tschanz.v_bro.versions.usecase.read_version_timeline.ReadVersionTimelineUseCase;
import com.tschanz.v_bro.element_classes.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.repo.presentation.controller.ConnectionController;
import com.tschanz.v_bro.versions.presentation.controller.VersionController;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;

import java.util.Properties;


public class MainController {
    private final MainView mainView;


    public MainController(
        Properties appProperties,
        MainView mainView,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc,
        ReadElementClassesUseCase readElementClassesUc,
        ReadElementDenominationsUseCase readElementDenominationsUc,
        ReadElementsUseCase readElementsUc,
        ReadVersionTimelineUseCase readVersionsUc,
        ReadFwdDependenciesUseCase readFwdDependenciesUc,
        ReadVersionAggregateUseCase readVersionAggregateUc
    ) {
        this.mainView = mainView;

        StatusBarController statusBarController = new StatusBarController(
            this.mainView.getStatusBarView()
        );

        ConnectionController connectionController = new ConnectionController(
            appProperties,
            mainView.getConnectionView(),
            statusBarController.getAppStatus(),
            openConnectionUc,
            closeConnectionUc
        );

        ElementClassController elementClassController = new ElementClassController(
            mainView.getElementClassView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentRepoConnection(),
            readElementClassesUc
        );

        ElementDenominationController elementDenominationController = new ElementDenominationController(
            mainView.getElementDenominationView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentRepoConnection(),
            elementClassController.getSelectElementClassAction(),
            readElementDenominationsUc
        );

        ElementController elementController = new ElementController(
            mainView.getElementView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentRepoConnection(),
            elementClassController.getSelectElementClassAction(),
            elementDenominationController.getSelectDenominationsAction(),
            readElementsUc
        );

        VersionFilterController versionFilterController = new VersionFilterController(
            mainView.getVersionFilterView()
        );

        VersionController versionController = new VersionController(
            mainView.getVersionsView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentRepoConnection(),
            elementClassController.getSelectElementClassAction(),
            elementController.getSelectElementAction(),
            versionFilterController.getSelectVersionFilterAction(),
            readVersionsUc
        );

        DependenciesController dependenciesController = new DependenciesController(
            mainView.getDependenciesView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentRepoConnection(),
            elementClassController.getSelectElementClassAction(),
            elementController.getSelectElementAction(),
            versionController.getSelectVersionAction(),
            readFwdDependenciesUc
        );

        VersionAggregateController versionAggregateController = new VersionAggregateController(
            mainView.getVersionAggregateView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentRepoConnection(),
            elementClassController.getSelectElementClassAction(),
            elementController.getSelectElementAction(),
            versionController.getSelectVersionAction(),
            readVersionAggregateUc
        );
    }


    public void start() {
        this.mainView.start();
    }
}
