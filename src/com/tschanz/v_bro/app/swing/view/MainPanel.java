package com.tschanz.v_bro.app.swing.view;

import com.tschanz.v_bro.elements.swing.view.ElementPanel;
import com.tschanz.v_bro.elements.swing.view.ElementView;
import com.tschanz.v_bro.repo.swing.view.ConnectionPanel;
import com.tschanz.v_bro.repo.swing.view.ConnectionView;
import com.tschanz.v_bro.versioning.swing.view.VersionsPanel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JFrame implements MainView {
    private final ConnectionPanel connectionView = new ConnectionPanel();
    private final ElementPanel elementView = new ElementPanel();
    private final VersionsPanel versionsView = new VersionsPanel();
    private final StatusBarPanel statusBarView = new StatusBarPanel();


    public MainPanel() {
        this.setTitle("V-Bro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 1000);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(this.connectionView);
        contentPanel.add(this.elementView);
        contentPanel.add(this.versionsView);

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
    public ElementView getElementView() {
        return this.elementView;
    }


    @Override
    public VersionsPanel getVersionsView() {
        return this.versionsView;
    }


    @Override
    public StatusBarView getStatusBarView() {
        return this.statusBarView;
    }
}

