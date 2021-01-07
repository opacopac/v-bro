package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.MainController;
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
    @FXML private AnchorPane dependencyElementClassView;
    @FXML private AnchorPane dependencyDenominationsView;
    @FXML private AnchorPane dependencyElementFilterView;
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
    @FXML private JfxDependencyElementClassView dependencyElementClassViewController;
    @FXML private JfxDependencyDenominationView dependencyDenominationsViewController;
    @FXML private JfxDependencyElementFilterView dependencyElementFilterViewController;
    @FXML private JfxDependencyListView dependenciesViewController;
    @FXML private JfxVersionAggregateView versionAggregateViewController;
    @FXML private JfxStatusBarView statusBarViewController;


    @Override
    public void bindViewModel(MainViewModel mainViewModel, MainController mainController) {
        this.statusBarViewController.bindViewModel(mainViewModel.appStatus);
        this.connectionViewController.bindViewModel(mainViewModel.quickConnectionList, mainViewModel.currentRepoConnection, mainController.getConnectionController());
        this.elementClassViewController.bindViewModel(mainViewModel.elementClasses, mainController.getElementClassController());
        this.elementDenominationViewController.bindViewModel(mainViewModel.elementDenominations, mainController.getElementDenominationController());
        this.elementViewController.bindViewModel(mainViewModel.currentElement, mainController.getElementController());
        this.versionFilterViewController.bindViewModel(mainViewModel.versionFilter, mainController.getVersionFilterController());
        this.versionViewController.bindViewModel(mainViewModel.versions, mainViewModel.versionFilter, mainController.getVersionController());
        this.dependencyFilterViewController.bindViewModel(mainViewModel.dependencyFilter, mainController.getDependencyFilterController());
        this.dependencyElementClassViewController.bindViewModel(mainViewModel.dependencyElementClasses, mainController.getDependencyElementClassController());
        this.dependencyDenominationsViewController.bindViewModel(mainViewModel.dependencyDenominations, mainController.getDependencyDenominationController());
        this.dependencyElementFilterViewController.bindViewModel(mainController.getDependencyElementFilterController());
        this.dependenciesViewController.bindViewModel(mainViewModel.fwdDependencies, mainViewModel.versionFilter, mainController.getDependencyListController());
        this.versionAggregateViewController.bindViewModel(mainViewModel.versionAggregate);
    }
}
