package com.tschanz.v_bro.app.swing.controller;

import com.tschanz.v_bro.app.swing.view.MainView;
import com.tschanz.v_bro.elements.swing.controller.ElementController;
import com.tschanz.v_bro.elements.usecase.read_element_denominations.ReadElementDenominationsUseCase;
import com.tschanz.v_bro.versioning.usecase.read_version_timeline.ReadVersionTimelineUseCase;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.repo.swing.controller.ConnectionController;
import com.tschanz.v_bro.versioning.swing.controller.VersionsController;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;


public class MainController {
    private final MainView mainView;


    public MainController(
        MainView mainView,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc,
        ReadElementClassesUseCase readElementClassesUc,
        ReadElementDenominationsUseCase readElementDenominationsUc,
        ReadElementsUseCase readElementsUc,
        ReadVersionTimelineUseCase readVersionsUc
    ) {
        this.mainView = mainView;

        StatusBarController statusBarController = new StatusBarController(
            this.mainView.getStatusBarView()
        );

        ConnectionController connectionController = new ConnectionController(
            mainView.getConnectionView(),
            statusBarController.getAppStatus(),
            openConnectionUc,
            closeConnectionUc
        );

        ElementController elementController = new ElementController(
            mainView.getElementView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentConnection(),
            readElementClassesUc,
            readElementDenominationsUc,
            readElementsUc
        );

        VersionsController versionsController = new VersionsController(
            mainView.getVersionsView(),
            statusBarController.getAppStatus(),
            connectionController.getCurrentConnection(),
            elementController.getSelectedElementClass(),
            elementController.getSelectedElement(),
            elementController.getSelectedVersionFilter(),
            readVersionsUc
        );
    }


    public void start() {
        this.mainView.start();
    }
}
