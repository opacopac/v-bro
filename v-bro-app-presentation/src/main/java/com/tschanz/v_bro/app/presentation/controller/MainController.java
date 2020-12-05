package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.actions.MainActions;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionUseCase;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassUseCase;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationUseCase;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionUseCase;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionUseCase;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCase;
import lombok.Getter;

import java.util.Properties;


public class MainController {
    @Getter private final ConnectionController connectionController;
    @Getter private final ElementClassController elementClassController;
    @Getter private final ElementDenominationController elementDenominationController;
    @Getter private final ElementController elementController;
    @Getter private final VersionFilterController versionFilterController;
    @Getter private final VersionController versionController;
    @Getter private final DependencyListController dependencyListController;


    public MainController(
        Properties appProperties,
        MainModel mainModel,
        MainActions mainActions,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc,
        SelectElementClassUseCase selectElementClassUc,
        SelectElementDenominationUseCase selectElementDenominationUc,
        SelectElementUseCase selectElementUc,
        SelectVersionUseCase selectVersionUc,
        SelectDependencyFilterUseCase selectDependencyFilterUc,
        SelectDependencyVersionUseCase selectDependencyVersionUc
    ) {
        this.connectionController = new ConnectionController(
            appProperties,
            mainModel.quickConnectionList,
            mainModel.currentRepoConnection,
            mainActions.connectToRepoAction,
            openConnectionUc,
            closeConnectionUc
        );

        this.elementClassController = new ElementClassController(
            mainModel.currentRepoConnection,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainActions.selectElementClassAction,
            selectElementClassUc
        );

        this.elementDenominationController = new ElementDenominationController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainActions.selectDenominationsAction,
            selectElementDenominationUc
        );

        this.elementController = new ElementController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainActions.selectElementAction,
            selectElementUc
        );

        this.versionFilterController = new VersionFilterController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainModel.elements,
            mainModel.dependencyFilter,
            mainActions.selectVersionFilterAction,
            selectElementUc
        );

        this.versionController = new VersionController(
            mainModel.currentRepoConnection,
            mainModel.elementClasses,
            mainModel.elements,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainActions.selectVersionAction,
            selectVersionUc
        );

        this.dependencyListController = new DependencyListController(
            mainModel.currentRepoConnection,
            mainModel.versionFilter,
            mainModel.dependencyFilter,
            mainActions.selectDependencyVersionAction,
            selectDependencyVersionUc
        );
    }
}
