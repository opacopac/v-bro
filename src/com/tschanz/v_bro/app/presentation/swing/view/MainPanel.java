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
        this.setPreferredSize(new Dimension(1200, 1000));
        //this.setSize(1200, 1000);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());


        this.initConnection(contentPanel);
        this.initElementClassAndDenominations(contentPanel);
        this.initElementSelection(contentPanel);
        this.initVersionFilter(contentPanel);
        this.initVersionTimeline(contentPanel);
        this.initDependencies(contentPanel);
        this.initVersionAggregate(contentPanel);

        this.getContentPane().add(contentPanel, BorderLayout.PAGE_START);
        this.getContentPane().add(this.statusBarPanel, BorderLayout.PAGE_END);
    }


    private void initConnection(JPanel contentPanel) {
        contentPanel.add(
            this.connectionPanel,
            this.createGBConstraint(0, 0)
        );
    }


    private void initElementClassAndDenominations(JPanel contentPanel) {
        contentPanel.add(
            this.createContainer(
                this.elementClassSelectionPanel,
                this.denominationSelectionPanel
            ),
            this.createGBConstraint(0, 1)
        );
    }


    private void initElementSelection(JPanel contentPanel) {
        contentPanel.add(
            this.elementSelectionPanel,
            this.createGBConstraint(0, 2)
        );
    }


    private void initVersionFilter(JPanel contentPanel) {
        contentPanel.add(
            this.versionFilterPanel,
            this.createGBConstraint(0, 3)
        );
    }


    private void initVersionTimeline(JPanel contentPanel) {
        contentPanel.add(
            this.versionTimeline,
            this.createGBConstraint(0, 4)
        );
    }


    private void initDependencies(JPanel contentPanel) {
        contentPanel.add(
            this.createContainer(
                new JLabel("Dependencies:"),
                new JLabel("(o) FWD   ( ) BWD")
            ),
            this.createGBConstraint(0, 5)
        );

        contentPanel.add(
            this.dependencyListPanel,
            this.createGBConstraint(0, 6)
        );
    }



    private void initVersionAggregate(JPanel contentPanel) {
        contentPanel.add(
            new JLabel("Data:"),
            this.createGBConstraint(1, 0)
        );

        contentPanel.add(
            this.versionAggregateTree,
            this.createGBConstraint(1, 1, 1, 6)
        );
    }


    private JPanel createContainer(JComponent... components) {
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        for (JComponent component: components) {
            container.add(component);
        }

        return container;
    }


    private GridBagConstraints createGBConstraint(int gridx, int gridy) {
        return this.createGBConstraint(gridx, gridy, 1, 1);
    }


    private GridBagConstraints createGBConstraint(int gridx, int gridy, int gridwidth) {
        return this.createGBConstraint(gridx, gridy, gridwidth, 1);
    }


    private GridBagConstraints createGBConstraint(int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;

        return gbc;
    }
}
