package com.tschanz.v_bro.app.swing;

import com.tschanz.v_bro.elements.swing.element_selection.ElementSelectionView;
import com.tschanz.v_bro.repo.swing.connection.ConnectionView;
import com.tschanz.v_bro.versioning.swing.dependencyselection.DependencySelectionView;
import com.tschanz.v_bro.elements.swing.element_class_selection.ElementClassSelectionView;
import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;
import com.tschanz.v_bro.versioning.swing.versionfilter.VersionFilterPanel;
import com.tschanz.v_bro.versioning.swing.versions.VersionsPanel;


public interface MainView {
    void start();

    ConnectionView getConnectionView();

    ElementClassSelectionView getElementClassSelectionView();

    ElementSelectionView getElementSelectionView();

    VersionFilterPanel getVersionFilterView();

    VersionsPanel getVersionsView();

    DependencySelectionView getDependencySelectionView();

    StatusBarView getStatusBarView();
}
