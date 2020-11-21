package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDependencyVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.view.DependenciesView;
import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Flow;


public class DependencyListPanel extends JPanel implements DependenciesView {
    public static final int DEP_PANEL_WIDTH = VersionTimeline.TIMELINE_WIDTH + 50;
    public static final int DEP_PANEL_HEIGHT = 550;
    private final JPanel contentPanel = new JPanel();
    private Flow.Publisher<VersionFilterItem> effectiveVersionFilter;
    private SelectDependencyVersionAction selectDependencyVersionAction;


    public DependencyListPanel() {
        this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane(this.contentPanel);
        scrollPane.setPreferredSize(new Dimension(DEP_PANEL_WIDTH, DEP_PANEL_HEIGHT));
        this.add(scrollPane);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList,
        Flow.Publisher<VersionFilterItem> versionFilter,
        SelectDependencyVersionAction selectDependencyVersionAction
    ) {
        this.effectiveVersionFilter = versionFilter;
        this.selectDependencyVersionAction = selectDependencyVersionAction;
        fwdDependencyList.subscribe(new GenericSubscriber<>(this::onFwdDependenciesChanged));
    }


    private void onFwdDependenciesChanged(List<FwdDependencyItem> fwdDependencyList) {
        this.contentPanel.removeAll();

        if (fwdDependencyList != null) {
            for (FwdDependencyItem dependency : fwdDependencyList) {
                DependencyListEntry dependencyListEntry = new DependencyListEntry();
                dependencyListEntry.bindViewModel(
                    dependency,
                    this.effectiveVersionFilter,
                    this.selectDependencyVersionAction
                );
                this.contentPanel.add(dependencyListEntry);
            }
        }

        this.repaint();
        this.revalidate();
    }
}
