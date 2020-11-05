package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.view.MainView;
import com.tschanz.v_bro.app.presentation.view.StatusBarView;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.StrokeBorder;
import java.awt.*;


public class MainPanel extends JFrame implements MainView {
    public static final int MAIN_WIDTH = 1500;
    public static final int MAIN_HEIGHT = 1000;

    private final ConnectionPanel connectionPanel = new ConnectionPanel();
    private final ElementClassSelectionPanel elementClassSelectionPanel = new ElementClassSelectionPanel();
    private final DenominatonSelectionPanel denominationSelectionPanel = new DenominatonSelectionPanel();
    private final ElementSelectionPanel elementSelectionPanel = new ElementSelectionPanel();
    private final VersionFilterPanel versionFilterPanel = new VersionFilterPanel();
    private final VersionTimeline versionTimeline = new VersionTimeline();
    private final DependencyFilterPanel dependencyFilterPanel = new DependencyFilterPanel();
    private final DependencyListPanel dependencyListPanel = new DependencyListPanel();
    private final VersionAggregateTree versionAggregateTree = new VersionAggregateTree();
    private final StatusBarPanel statusBarPanel = new StatusBarPanel();


    public MainPanel() {
        this.InitView();
    }


    @Override
    public void bindViewModel(MainModel mainModel) {
        this.statusBarPanel.bindStatus(mainModel.appStatus);
        this.connectionPanel.bindQuickConnectionList(mainModel.quickConnectionList);
        this.connectionPanel.bindCurrentRepoConnection(mainModel.currentRepoConnection);
        this.connectionPanel.bindConnectToRepoAction(mainModel.connectToRepoAction);
        this.elementClassSelectionPanel.bindElementClassList(mainModel.elementClasses);
        this.elementClassSelectionPanel.bindSelectElementClassAction(mainModel.selectedElementClass);
        this.denominationSelectionPanel.bindDenominationList(mainModel.elementDenominations);
        this.denominationSelectionPanel.bindSelectDenominationsAction(mainModel.selectedDenominations);
        this.elementSelectionPanel.bindElementList(mainModel.elements);
        this.elementSelectionPanel.bindSelectElementAction(mainModel.selectedElement);
        this.versionFilterPanel.bindInitialVersionFilter(mainModel.versionFilter);
        this.versionFilterPanel.bindSelectVersionFilterAction(mainModel.selectedVersionFilter);
        this.versionTimeline.bindEffectiveVersionFilter(mainModel.effectiveVersionFilter);
        this.versionTimeline.bindVersionList(mainModel.versions);
        this.versionTimeline.bindSelectVersionAction(mainModel.selectedVersion);
        this.dependencyFilterPanel.bindInitialDependencyFilter(mainModel.dependencyFilter);
        this.dependencyFilterPanel.bindSelectDependencyFilterAction(mainModel.selectedDependencyFilter);
        this.dependencyListPanel.bindEffectiveVersionFilter(mainModel.effectiveVersionFilter);
        this.dependencyListPanel.bindFwdDependencyList(mainModel.fwdDependencies);
        this.versionAggregateTree.bindVersionAggregate(mainModel.versionAggregate);
    }


    @Override
    public void start() {
        this.pack();
        this.setVisible(true);
    }


    @Override public StatusBarView getStatusBarView() { return this.statusBarPanel; }


    private void InitView() {
        this.setTitle("V-Bro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(MAIN_WIDTH, MAIN_HEIGHT));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());

        this.initConnection(contentPanel);
        this.initElementClassAndDenominations(contentPanel);
        this.initElementSelection(contentPanel);
        this.initVersionFilter(contentPanel);
        this.initVersionTimeline(contentPanel);
        this.initDependencies(contentPanel);
        this.initVersionAggregate(contentPanel);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
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
        GridBagConstraints c = this.createGBConstraint(0, 4);
        c.insets = new Insets(10, 29, 10, 10);
        contentPanel.add(
            this.versionTimeline,
            c
        );
    }


    private void initDependencies(JPanel contentPanel) {
        contentPanel.add(
            this.dependencyFilterPanel,
            this.createGBConstraint(0, 5)
        );

        contentPanel.add(
            this.dependencyListPanel,
            this.createGBConstraint(0, 6, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL)
        );
    }



    private void initVersionAggregate(JPanel contentPanel) {
        contentPanel.add(
            new JLabel("Data:"),
            this.createGBConstraint(1, 0)
        );

        contentPanel.add(
            this.versionAggregateTree,
            this.createGBConstraint(1, 1, 1, 6, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH)
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
        return this.createGBConstraint(
            gridx,
            gridy,
            1,
            1,
            GridBagConstraints.FIRST_LINE_START,
            GridBagConstraints.NONE
        );
    }


    private GridBagConstraints createGBConstraint(
        int gridx,
        int gridy,
        int gridwidth,
        int gridheight,
        int anchor,
        int fill
    ) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        gbc.fill = fill;

        return gbc;
    }
}
