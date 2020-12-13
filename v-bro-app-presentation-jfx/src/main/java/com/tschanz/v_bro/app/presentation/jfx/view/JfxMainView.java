package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.MainActions;
import com.tschanz.v_bro.app.presentation.view.MainView;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;



public class JfxMainView implements MainView {
    @FXML private AnchorPane connectionView;
    @FXML private AnchorPane elementClassView;
    @FXML private AnchorPane elementDenominationView;
    @FXML private AnchorPane elementView;
    @FXML private AnchorPane versionFilterView;
    @FXML private AnchorPane versionView;
    @FXML private AnchorPane dependencyFilterView;
    @FXML private AnchorPane dependenciesView;
    @FXML private AnchorPane versionAggregateView;
    @FXML private AnchorPane statusBarView;
    @FXML private JfxConnectionView connectionViewController;
    @FXML private JfxElementClassView elementClassViewController;
    @FXML private JfxElementDenominationView elementDenominationViewController;
    @FXML private JfxElementView elementViewController;
    @FXML private JfxVersionFilterView versionFilterViewController;
    @FXML private JfxVersionView versionViewController;
    @FXML private JfxDependencyFilterView dependencyFilterViewController;
    @FXML private JfxDependencyListView dependenciesViewController;
    @FXML private JfxVersionAggregateView versionAggregateViewController;
    @FXML private JfxStatusBarView statusBarViewController;


    @Override
    public void bindViewModel(MainViewModel mainViewModel, MainActions mainActions) {
        this.statusBarViewController.bindViewModel(mainViewModel.appStatus);
        this.connectionViewController.bindViewModel(mainViewModel.quickConnectionList, mainActions.connectToRepoAction, mainViewModel.currentRepoConnection);
        this.elementClassViewController.bindViewModel(mainViewModel.elementClasses, mainActions.selectElementClassAction);
        this.elementDenominationViewController.bindViewModel(mainViewModel.elementDenominations, mainActions.selectDenominationsAction);
        this.elementViewController.bindViewModel(mainViewModel.elements, mainActions.selectElementAction, mainActions.queryElementAction);
        this.versionFilterViewController.bindViewModel(mainViewModel.versionFilter, mainActions.selectVersionFilterAction);
        this.versionViewController.bindViewModel(mainViewModel.versions, mainViewModel.effectiveVersionFilter, mainActions.selectVersionAction);
        this.dependencyFilterViewController.bindViewModel(mainViewModel.dependencyFilter, mainActions.selectDependencyFilterAction);
        this.dependenciesViewController.bindViewModel(mainViewModel.fwdDependencies, mainViewModel.effectiveVersionFilter, mainActions.selectDependencyVersionAction);
        this.versionAggregateViewController.bindViewModel(mainViewModel.versionAggregate);
    }
}
