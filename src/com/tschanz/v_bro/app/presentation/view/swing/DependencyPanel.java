package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;

import javax.swing.*;
import java.util.Collections;
import java.util.concurrent.Flow;


public class DependencyPanel extends JPanel {
    private final JLabel dependencyName = new JLabel("");
    private final VersionTimeline versionTimeline = new VersionTimeline();
    private final BehaviorSubject<java.util.List<VersionItem>> versionList = new BehaviorSubject<>(Collections.emptyList()); // TODO => model


    public DependencyPanel() {
        //this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(dependencyName);
        this.add(this.versionTimeline);
        this.versionTimeline.bindVersionList(this.versionList);
    }


    public void setEffectiveVersionFilter(Flow.Publisher<VersionFilterItem> versionFilter) {
        this.versionTimeline.bindEffectiveVersionFilter(versionFilter);
    }


    public void setDependency(FwdDependencyItem dependency) {
        this.dependencyName.setText(this.createDependencyName(dependency));
        this.versionList.next(dependency.getVersions());

        this.repaint();
        this.revalidate();
    }


    private String createDependencyName(FwdDependencyItem dependency) {
        return dependency.elementName() + " - " + dependency.elementId();
    }
}
