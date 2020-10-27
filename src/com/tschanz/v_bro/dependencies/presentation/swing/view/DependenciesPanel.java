package com.tschanz.v_bro.dependencies.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.dependencies.presentation.swing.viewmodel.FwdDependencyItem;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Flow;


public class DependenciesPanel extends JPanel implements DependenciesView {
    public DependenciesPanel() {
        this.add(new JLabel("Dependencies"));
    }


    @Override
    public void bindFwdDependencyList(Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList) {
        fwdDependencyList.subscribe(new GenericSubscriber<>(this::onFwdDependenciesChanged));
    }


    private void onFwdDependenciesChanged(List<FwdDependencyItem> fwdDependencyList) {
        // TODO
    }
}
