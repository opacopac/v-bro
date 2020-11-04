package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.view.DependenciesView;
import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Flow;


public class DependencyListPanel extends JPanel implements DependenciesView {
    private Flow.Publisher<VersionFilterItem> effectiveVersionFilter;


    public DependencyListPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
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
        this.removeAll();

        if (fwdDependencyList != null) {
            for (FwdDependencyItem dependency : fwdDependencyList) {
                DependencyPanel dependencyPanel = new DependencyPanel();
                dependencyPanel.setEffectiveVersionFilter(this.effectiveVersionFilter);
                dependencyPanel.setDependency(dependency);
                this.add(dependencyPanel);
            }
        }

        this.repaint();
        this.revalidate();
    }
}
