package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionUseCase;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.open_repo.OpenRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCase;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCase;
import lombok.Getter;

import java.util.Properties;


@Getter
public class MainControllerImpl implements MainController {
    private final ConnectionController connectionController;
    private final ElementClassController elementClassController;
    private final ElementDenominationController elementDenominationController;
    private final ElementController elementController;
    private final VersionFilterController versionFilterController;
    private final VersionController versionController;
    private final DependencyFilterController dependencyFilterController;
    private final DependencyListController dependencyListController;


    public MainControllerImpl(
        Properties appProperties,
        MainViewModel mainViewModel,
        OpenRepoUseCase openRepoUseCase,
        CloseRepoUseCase closeRepoUseCase,
        OpenElementClassUseCase openElementClassUc,
        SelectDenominationsUseCase selectDenominationsUc,
        QueryElementsUseCase queryElementsUc,
        OpenElementUseCase openElementUc,
        SelectVersionFilterUseCase selectVersionFilterUc,
        OpenVersionUseCase openVersionUc,
        SelectDependencyFilterUseCase selectDependencyFilterUc,
        OpenDependencyVersionUseCase openDependencyVersionUc
    ) {
        this.connectionController = new ConnectionControllerImpl(
            appProperties,
            mainViewModel.quickConnectionList,
            mainViewModel.currentRepoConnection,
            openRepoUseCase,
            closeRepoUseCase
        );
        this.elementClassController = new ElementClassControllerImpl(openElementClassUc);
        this.elementDenominationController = new ElementDenominationControllerImpl(selectDenominationsUc);
        this.elementController = new ElementControllerImpl(queryElementsUc, openElementUc);
        this.versionFilterController = new VersionFilterControllerImpl(selectVersionFilterUc);
        this.versionController = new VersionControllerImpl(openVersionUc);
        this.dependencyFilterController = new DependencyFilterControllerImpl(selectDependencyFilterUc);
        this.dependencyListController = new DependencyListControllerImpl(openDependencyVersionUc);
    }
}
