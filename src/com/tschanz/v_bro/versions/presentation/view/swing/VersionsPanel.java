package com.tschanz.v_bro.versions.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.dependencies.presentation.view.swing.DependencyListPanel;
import com.tschanz.v_bro.dependencies.presentation.view.DependenciesView;
import com.tschanz.v_bro.version_aggregates.presentation.view.swing.VersionAggregateTree;
import com.tschanz.v_bro.dependencies.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.version_aggregates.presentation.view.VersionAggregateView;
import com.tschanz.v_bro.version_aggregates.presentation.viewmodel.VersionAggregateItem;
import com.tschanz.v_bro.versions.presentation.view.VersionsView;
import com.tschanz.v_bro.versions.presentation.viewmodel.VersionItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Flow;


public class VersionsPanel extends JPanel implements VersionsView, DependenciesView, VersionAggregateView {
    private final VersionTimeline timeline = new VersionTimeline();
    private final DependencyListPanel dependencies = new DependencyListPanel();
    private final VersionAggregateTree aggregateTree = new VersionAggregateTree();


    public VersionsPanel() {
        // title
        JPanel row1 = new JPanel();
        row1.setLayout(new FlowLayout(FlowLayout.LEADING));
        row1.add(new JLabel("Versions:"));

        // version timeline
        JPanel row2 = new JPanel();
        row2.setLayout(new FlowLayout(FlowLayout.LEADING));
        row2.add(this.timeline);
        this.add(row2);

        // dependencies & aggregate tree
        JPanel row3 = new JPanel();
        row3.setLayout(new FlowLayout(FlowLayout.LEADING));
        row3.add(this.dependencies);
        row3.add(this.aggregateTree);
        this.add(row3);

        // container
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(row1);
        this.add(row2);
        this.add(row3);
    }


    @Override
    public void bindVersionList(Flow.Publisher<List<VersionItem>> versionList) {
        this.timeline.bindVersionList(versionList);
    }


    @Override
    public void bindSelectVersionAction(BehaviorSubject<VersionItem> selectVersionAction) {
        this.timeline.bindSelectVersionAction(selectVersionAction);
    }


    @Override
    public void bindFwdDependencyList(Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList) {
        this.dependencies.bindFwdDependencyList(fwdDependencyList);
    }


    @Override
    public void bindVersionAggregate(Flow.Publisher<VersionAggregateItem> versionAggregate) {
        this.aggregateTree.bindVersionAggregate(versionAggregate);
    }
}
