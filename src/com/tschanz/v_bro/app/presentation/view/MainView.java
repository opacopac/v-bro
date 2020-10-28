package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.dependencies.presentation.view.DependenciesView;
import com.tschanz.v_bro.element_classes.presentation.view.ElementClassView;
import com.tschanz.v_bro.element_classes.presentation.view.ElementDenominationView;
import com.tschanz.v_bro.elements.presentation.view.ElementView;
import com.tschanz.v_bro.repo.presentation.view.ConnectionView;
import com.tschanz.v_bro.version_aggregates.presentation.view.VersionAggregateView;
import com.tschanz.v_bro.versions.presentation.view.VersionFilterView;
import com.tschanz.v_bro.versions.presentation.view.VersionsView;


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
