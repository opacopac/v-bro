package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.MainActions;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionUseCase;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.open_repo.OpenRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCase;
import lombok.Getter;

import java.util.Properties;


@Getter
public class MainController {
    private final ConnectionController connectionController;
    private final ElementClassController elementClassController;
    private final ElementDenominationController elementDenominationController;
    private final ElementController elementController;
    private final VersionFilterController versionFilterController;
    private final VersionController versionController;
    private final DependencyListController dependencyListController;


    public MainController(
        Properties appProperties,
        MainViewModel mainViewModel,
        MainActions mainActions,
        OpenRepoUseCase openRepoUseCase,
        CloseRepoUseCase closeRepoUseCase,
        OpenElementClassUseCase openElementClassUc,
        SelectDenominationsUseCase selectDenominationsUc,
        QueryElementsUseCase queryElementsUc,
        OpenElementUseCase openElementUc,
        SelectVersionFilterUseCase selectVersionFilterUc,
        OpenVersionUseCase openVersionUc,
        OpenDependencyVersionUseCase openDependencyVersionUc
    ) {
        this.connectionController = new ConnectionController(
            appProperties,
            mainViewModel.quickConnectionList,
            mainViewModel.currentRepoConnection,
            mainActions.connectToRepoAction,
            openRepoUseCase,
            closeRepoUseCase
        );

        this.elementClassController = new ElementClassController(
            mainActions.selectElementClassAction,
            openElementClassUc
        );

        this.elementDenominationController = new ElementDenominationController(
            mainActions.selectDenominationsAction,
            selectDenominationsUc
        );

        this.elementController = new ElementController(
            mainActions.queryElementAction,
            mainActions.selectElementAction,
            queryElementsUc,
            openElementUc
        );

        this.versionFilterController = new VersionFilterController(
            mainActions.selectVersionFilterAction,
            selectVersionFilterUc
        );

        this.versionController = new VersionController(
            mainActions.selectVersionAction,
            openVersionUc
        );

        this.dependencyListController = new DependencyListController(
            mainActions.selectDependencyVersionAction,
            openDependencyVersionUc
        );
    }
}
