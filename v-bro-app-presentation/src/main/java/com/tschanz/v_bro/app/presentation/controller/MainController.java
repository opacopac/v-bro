package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionUseCase;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassUseCase;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationUseCase;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionUseCase;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionUseCase;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCase;

import java.util.Properties;


public class MainController {
    public MainController(
        Properties appProperties,
        MainModel mainModel,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc,
        SelectElementClassUseCase selectElementClassUc,
        SelectElementDenominationUseCase selectElementDenominationUc,
        SelectElementUseCase selectElementUc,
        SelectVersionUseCase selectVersionUc,
        SelectDependencyFilterUseCase selectDependencyFilterUc,
        SelectDependencyVersionUseCase selectDependencyVersionUc
    ) {
        ConnectionController connectionController = new ConnectionController(
            appProperties,
            mainModel.quickConnectionList,
            mainModel.currentRepoConnection,
            mainModel.connectToRepoAction,
            openConnectionUc,
            closeConnectionUc
        );

        ElementClassController elementClassController = new ElementClassController(
            mainModel.currentRepoConnection,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainModel.selectElementClassAction,
            selectElementClassUc
        );

        ElementDenominationController elementDenominationController = new ElementDenominationController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainModel.selectDenominationsAction,
            selectElementDenominationUc
        );

        ElementController elementController = new ElementController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainModel.selectElementAction,
            selectElementUc
        );

        VersionFilterController versionFilterController = new VersionFilterController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainModel.elements,
            mainModel.dependencyFilter,
            mainModel.selectVersionFilterAction,
            selectElementUc
        );

        VersionController versionController = new VersionController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainModel.elements,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainModel.selectVersionAction,
            selectVersionUc
        );


        DependencyListController dependencyListController = new DependencyListController(
            mainModel.currentRepoConnection,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainModel.selectDependencyVersionAction,
            selectDependencyVersionUc
        );
    }
}
