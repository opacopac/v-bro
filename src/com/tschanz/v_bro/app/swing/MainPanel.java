package com.tschanz.v_bro.app.swing;

import com.tschanz.v_bro.common.swing.statusbar.StatusBarPanel;
import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;
import com.tschanz.v_bro.repo.swing.connection.ConnectionPanel;
import com.tschanz.v_bro.repo.swing.connection.ConnectionView;
import com.tschanz.v_bro.versioning.swing.dependencyselection.DependencySelectionPanel;
import com.tschanz.v_bro.versioning.swing.dependencyselection.DependencySelectionView;
import com.tschanz.v_bro.elements.swing.elementselection.ElementSelectionPanel;
import com.tschanz.v_bro.elements.swing.elementselection.ElementSelectionView;
import com.tschanz.v_bro.versioning.swing.versionfilter.VersionFilterPanel;
import com.tschanz.v_bro.versioning.swing.versions.VersionsPanel;

import javax.swing.*;
import java.awt.*;


public class MainPanel extends JFrame implements MainView {
    private final ConnectionPanel connectionView = new ConnectionPanel();
    private final ElementSelectionPanel elementSelectionView = new ElementSelectionPanel();
    private final VersionFilterPanel versionFilterView = new VersionFilterPanel();
    private final VersionsPanel versionsView = new VersionsPanel();
    private final DependencySelectionPanel dependencySelectionPanel = new DependencySelectionPanel();
    private final StatusBarPanel statusBarView = new StatusBarPanel();


    public MainPanel() {
        this.setTitle("V-Bro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 1000);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(this.connectionView);
        contentPanel.add(this.elementSelectionView);
        contentPanel.add(this.versionFilterView);
        contentPanel.add(this.versionsView);
        contentPanel.add(this.dependencySelectionPanel);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.getContentPane().add(this.statusBarView, BorderLayout.PAGE_END);
    }


    @Override
    public void start() {
        this.pack();
        this.setVisible(true);
    }


    @Override
    public ConnectionView getConnectionView() {
        return this.connectionView;
    }

    @Override
    public ElementSelectionView getElementSelectionView() {
        return this.elementSelectionView;
    }

    @Override
    public VersionFilterPanel getVersionFilterView() {
        return this.versionFilterView;
    }

    @Override
    public VersionsPanel getVersionsView() {
        return this.versionsView;
    }

    @Override
    public StatusBarView getStatusBarView() {
        return this.statusBarView;
    }

    @Override
    public DependencySelectionView getDependencySelectionView() {
        return this.dependencySelectionPanel;
    }
}
