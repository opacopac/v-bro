package com.tschanz.v_bro.app.presentation.swing.view;

import com.tschanz.v_bro.dependencies.presentation.swing.view.DependenciesView;
import com.tschanz.v_bro.element_classes.presentation.swing.view.ElementClassView;
import com.tschanz.v_bro.element_classes.presentation.swing.view.ElementDenominationView;
import com.tschanz.v_bro.elements.presentation.swing.view.ElementView;
import com.tschanz.v_bro.repo.presentation.swing.view.ConnectionView;
import com.tschanz.v_bro.version_aggregates.presentation.swing.view.VersionAggregateView;
import com.tschanz.v_bro.versions.presentation.swing.view.VersionFilterView;
import com.tschanz.v_bro.versions.presentation.swing.view.VersionsView;


public interface MainView {
    void start();

    ConnectionView getConnectionView();

    ElementClassView getElementClassView();

    ElementDenominationView getElementDenominationView();

    ElementView getElementView();

    VersionFilterView getVersionFilterView();

    VersionsView getVersionsView();

    DependenciesView getDependenciesView();

    VersionAggregateView getVersionAggregateView();

    StatusBarView getStatusBarView();
}
