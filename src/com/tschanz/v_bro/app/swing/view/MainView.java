package com.tschanz.v_bro.app.swing.view;

import com.tschanz.v_bro.elements.swing.view.ElementView;
import com.tschanz.v_bro.repo.swing.view.ConnectionView;
import com.tschanz.v_bro.versioning.swing.view.VersionsPanel;


public interface MainView {
    void start();

    ConnectionView getConnectionView();

    ElementView getElementView();

    VersionsPanel getVersionsView();

    StatusBarView getStatusBarView();
}
