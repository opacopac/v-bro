package com.tschanz.v_bro.app.presentation.view.swing;

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


    public DependencyListPanel() {
        this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane(this.contentPanel);
        scrollPane.setPreferredSize(new Dimension(DEP_PANEL_WIDTH, DEP_PANEL_HEIGHT));
        this.add(scrollPane);
    }


    @Override
    public void bindEffectiveVersionFilter(Flow.Publisher<VersionFilterItem> versionFilter) {
        this.effectiveVersionFilter = versionFilter;
    }


    @Override
    public void bindFwdDependencyList(Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList) {
        fwdDependencyList.subscribe(new GenericSubscriber<>(this::onFwdDependenciesChanged));
    }


    private void onFwdDependenciesChanged(List<FwdDependencyItem> fwdDependencyList) {
        this.contentPanel.removeAll();

        if (fwdDependencyList != null) {
            for (FwdDependencyItem dependency : fwdDependencyList) {
                DependencyEntry dependencyEntry = new DependencyEntry();
                dependencyEntry.setEffectiveVersionFilter(this.effectiveVersionFilter);
                dependencyEntry.setDependency(dependency);
                this.contentPanel.add(dependencyEntry);
            }
        }

        this.repaint();
        this.revalidate();
    }
}
