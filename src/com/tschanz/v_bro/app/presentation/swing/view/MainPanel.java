package com.tschanz.v_bro.app.presentation.swing.view;

import com.tschanz.v_bro.dependencies.presentation.swing.view.DependencyListPanel;
import com.tschanz.v_bro.dependencies.presentation.swing.view.DependenciesView;
import com.tschanz.v_bro.element_classes.presentation.swing.view.DenominatonSelectionPanel;
import com.tschanz.v_bro.element_classes.presentation.swing.view.ElementClassSelectionPanel;
import com.tschanz.v_bro.element_classes.presentation.swing.view.ElementClassView;
import com.tschanz.v_bro.element_classes.presentation.swing.view.ElementDenominationView;
import com.tschanz.v_bro.elements.presentation.swing.view.ElementSelectionPanel;
import com.tschanz.v_bro.elements.presentation.swing.view.ElementView;
import com.tschanz.v_bro.version_aggregates.presentation.swing.view.VersionAggregateView;
import com.tschanz.v_bro.versions.presentation.swing.view.*;
import com.tschanz.v_bro.repo.presentation.swing.view.ConnectionPanel;
import com.tschanz.v_bro.repo.presentation.swing.view.ConnectionView;
import com.tschanz.v_bro.version_aggregates.presentation.swing.view.VersionAggregateTree;

import javax.swing.*;
import java.awt.*;


public class MainPanel extends JFrame implements MainView {
    private final ConnectionPanel connectionPanel = new ConnectionPanel();
    private final ElementClassSelectionPanel elementClassSelectionPanel = new ElementClassSelectionPanel();
    private final DenominatonSelectionPanel denominationSelectionPanel = new DenominatonSelectionPanel();
    private final ElementSelectionPanel elementSelectionPanel = new ElementSelectionPanel();
    private final VersionFilterPanel versionFilterPanel = new VersionFilterPanel();
    private final VersionTimeline versionTimeline = new VersionTimeline();
    private final DependencyListPanel dependencyListPanel = new DependencyListPanel();
    private final VersionAggregateTree versionAggregateTree = new VersionAggregateTree();

    private final StatusBarPanel statusBarPanel = new StatusBarPanel();


    public MainPanel() {
        this.InitView();
    }


    @Override
    public void start() {
        this.pack();
        this.setVisible(true);
    }


    @Override public ConnectionView getConnectionView() { return this.connectionPanel; }
    @Override public ElementClassView getElementClassView() { return this.elementClassSelectionPanel; }
    @Override public ElementDenominationView getElementDenominationView() { return this.denominationSelectionPanel; }
    @Override public ElementView getElementView() { return this.elementSelectionPanel; }
    @Override public VersionFilterView getVersionFilterView() { return this.versionFilterPanel; }
    @Override public VersionsView getVersionsView() { return this.versionTimeline; }
    @Override public DependenciesView getDependenciesView() { return this.dependencyListPanel; }
    @Override public VersionAggregateView getVersionAggregateView() { return this.versionAggregateTree; }
    @Override public StatusBarView getStatusBarView() { return this.statusBarPanel; }


    private void InitView() {
        this.setTitle("V-Bro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 1000);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        this.getContentPane().add(contentPanel, BorderLayout.PAGE_START);

        this.initConnection(contentPanel);
        this.initElementClassAndDenominations(contentPanel);
        this.initElementAndVersionFilter(contentPanel);
        this.initVersionTimeline(contentPanel);
        this.initDependencyAndVersionAggregate(contentPanel);

        this.getContentPane().add(this.statusBarPanel, BorderLayout.PAGE_END);
    }


    private void initConnection(JPanel contentPanel) {
        contentPanel.add(this.connectionPanel);
    }


    private void initElementClassAndDenominations(JPanel contentPanel) {
        JPanel row1 = new JPanel();
        row1.setLayout(new FlowLayout(FlowLayout.LEADING));
        row1.add(new JLabel("Element Classes:"));
        contentPanel.add(row1);

        JPanel row2 = new JPanel();
        row2.setLayout(new FlowLayout(FlowLayout.LEADING));
        row2.add(this.elementClassSelectionPanel);
        row2.add(this.denominationSelectionPanel);
        contentPanel.add(row2);
    }


    private void initElementAndVersionFilter(JPanel contentPanel) {
        JPanel row1 = new JPanel();
        row1.setLayout(new FlowLayout(FlowLayout.LEADING));
        row1.add(new JLabel("Elements:"));
        contentPanel.add(row1);

        JPanel row2 = new JPanel();
        row2.setLayout(new FlowLayout(FlowLayout.LEADING));
        row2.add(this.elementSelectionPanel);
        row2.add(this.versionFilterPanel);
        contentPanel.add(row2);
    }


    private void initVersionTimeline(JPanel contentPanel) {
        JPanel row1 = new JPanel();
        row1.setLayout(new FlowLayout(FlowLayout.LEADING));
        row1.add(new JLabel("Versions:"));
        contentPanel.add(row1);

        JPanel row2 = new JPanel();
        row2.setLayout(new FlowLayout(FlowLayout.LEADING));
        row2.add(this.versionTimeline);
        contentPanel.add(row2);
    }


    private void initDependencyAndVersionAggregate(JPanel contentPanel) {
        JPanel row1 = new JPanel();
        row1.setLayout(new FlowLayout(FlowLayout.LEADING));
        row1.add(this.dependencyListPanel);
        row1.add(this.versionAggregateTree);
        contentPanel.add(row1);
    }
}
