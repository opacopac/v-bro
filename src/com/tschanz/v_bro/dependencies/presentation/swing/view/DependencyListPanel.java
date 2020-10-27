package com.tschanz.v_bro.dependencies.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.dependencies.presentation.swing.viewmodel.FwdDependencyItem;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Flow;


public class DependencyListPanel extends JPanel implements DependenciesView {
    public DependencyListPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
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
                dependencyPanel.setDependency(dependency);
                this.add(dependencyPanel);
            }
        }

        this.repaint();
        this.revalidate();
    }
}
