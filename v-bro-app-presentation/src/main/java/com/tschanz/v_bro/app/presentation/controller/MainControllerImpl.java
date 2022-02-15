package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionUseCase;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.open_repo.OpenRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.app.usecase.read_quick_connections.ReadQuickConnectionsUseCase;
import com.tschanz.v_bro.app.usecase.refresh_view.RefreshViewUseCase;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_denominations.SelectDependencyDenominationsUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_direction.SelectDependencyDirectionUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_element_filter.SelectDependencyElementFilterUseCase;
import com.tschanz.v_bro.app.usecase.select_version_aggregate_history.SelectVersionAggregateHistoryUseCase;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCase;
import lombok.Getter;


@Getter
public class MainControllerImpl implements MainController {
    private final ProgressController progressController;
    private final ConnectionController connectionController;
    private final ElementClassController elementClassController;
    private final ElementDenominationController elementDenominationController;
    private final RefreshController refreshController;
    private final ElementController elementController;
    private final VersionFilterController versionFilterController;
    private final VersionController versionController;
    private final DependencyDirectionController dependencyDirectionController;
    private final DependencyElementClassController dependencyElementClassController;
    private final DependencyDenominationController dependencyDenominationController;
    private final DependencyElementFilterController dependencyElementFilterController;
    private final DependencyListController dependencyListController;
    private final VersionAggregateHistoryController versionAggregateHistoryController;


    public MainControllerImpl(
        MainViewModel mainViewModel,
        ReadQuickConnectionsUseCase readQuickConnectionsUc,
        OpenRepoUseCase openRepoUseCase,
        CloseRepoUseCase closeRepoUseCase,
        OpenElementClassUseCase openElementClassUc,
        SelectDenominationsUseCase selectDenominationsUc,
        RefreshViewUseCase refreshViewUc,
        QueryElementsUseCase queryElementsUc,
        OpenElementUseCase openElementUc,
        SelectVersionFilterUseCase selectVersionFilterUc,
        OpenVersionUseCase openVersionUc,
        SelectDependencyDirectionUseCase selectDependencyDirectionUc,
        SelectDependencyElementClassUseCase selectDependencyElementClassUc,
        SelectDependencyDenominationsUseCase selectDependencyDenominationsUc,
        SelectDependencyElementFilterUseCase selectDependencyElementFilterUc,
        SelectVersionAggregateHistoryUseCase selectVersionAggregateHistoryUc,
        OpenDependencyVersionUseCase openDependencyVersionUc
    ) {
        this.progressController = new ProgressControllerImpl(mainViewModel.progressStatus);
        this.connectionController = new ConnectionControllerImpl(
            mainViewModel.currentRepoConnection,
            readQuickConnectionsUc,
            openRepoUseCase,
            closeRepoUseCase,
            this.progressController
        );
        this.elementClassController = new ElementClassControllerImpl(openElementClassUc, this.progressController);
        this.elementDenominationController = new ElementDenominationControllerImpl(selectDenominationsUc, this.progressController);
        this.refreshController = new RefreshControllerImpl(refreshViewUc, this.progressController);
        this.elementController = new ElementControllerImpl(queryElementsUc, openElementUc, this.progressController);
        this.versionFilterController = new VersionFilterControllerImpl(selectVersionFilterUc, this.progressController);
        this.versionController = new VersionControllerImpl(openVersionUc, this.progressController);
        this.dependencyDirectionController = new DependencyDirectionControllerImpl(selectDependencyDirectionUc, this.progressController);
        this.dependencyElementClassController = new DependencyElementClassControllerImpl(selectDependencyElementClassUc, this.progressController);
        this.dependencyDenominationController = new DependencyDenominationControllerImpl(selectDependencyDenominationsUc, this.progressController);
        this.dependencyElementFilterController = new DependencyElementFilterControllerImpl(selectDependencyElementFilterUc, this.progressController);
        this.dependencyListController = new DependencyListControllerImpl(openDependencyVersionUc, this.progressController);
        this.versionAggregateHistoryController = new VersionAggregateHistoryControllerImpl(selectVersionAggregateHistoryUc, this.progressController);
    }
}
