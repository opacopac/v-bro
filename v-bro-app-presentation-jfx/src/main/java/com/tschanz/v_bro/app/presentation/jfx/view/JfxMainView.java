package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.MainActions;
import com.tschanz.v_bro.app.presentation.view.MainView;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;

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
    public void bindViewModel(MainModel mainModel, MainActions mainActions) {
        this.statusBarViewController.bindViewModel(mainModel.appStatus);
        this.connectionViewController.bindViewModel(mainModel.quickConnectionList, mainActions.connectToRepoAction, mainModel.currentRepoConnection);
        this.elementClassViewController.bindViewModel(mainModel.elementClasses, mainActions.selectElementClassAction);
        this.elementDenominationViewController.bindViewModel(mainModel.elementDenominations, mainActions.selectDenominationsAction);
        this.elementViewController.bindViewModel(mainModel.elements, mainActions.selectElementAction, mainActions.queryElementAction);
        this.versionFilterViewController.bindViewModel(mainModel.versionFilter, mainActions.selectVersionFilterAction);
        this.versionViewController.bindViewModel(mainModel.versions, mainModel.effectiveVersionFilter, mainActions.selectVersionAction);
        this.dependencyFilterViewController.bindViewModel(mainModel.dependencyFilter, mainActions.selectDependencyFilterAction);
        this.dependenciesViewController.bindViewModel(mainModel.fwdDependencies, mainModel.effectiveVersionFilter, mainActions.selectDependencyVersionAction);
        this.versionAggregateViewController.bindViewModel(mainModel.versionAggregate);
    }
}
