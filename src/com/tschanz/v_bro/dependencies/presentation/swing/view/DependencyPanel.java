package com.tschanz.v_bro.dependencies.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.dependencies.presentation.swing.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.versions.presentation.swing.view.VersionTimeline;
import com.tschanz.v_bro.versions.presentation.swing.viewmodel.VersionItem;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;


public class DependencyPanel extends JPanel {
    private JLabel dependencyName = new JLabel("");
    private VersionTimeline versionTimeline = new VersionTimeline();
    private BehaviorSubject<java.util.List<VersionItem>> versionList = new BehaviorSubject<>(Collections.emptyList());


    public DependencyPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.add(this.versionTimeline);
        this.versionTimeline.bindVersionList(this.versionList);
    }


    public void setDependency(FwdDependencyItem dependency) {
        this.dependencyName.setText(this.getDependencyName(dependency));
        this.versionList.next(dependency.getVersions());

        this.repaint();
        this.revalidate();
    }


    private String getDependencyName(FwdDependencyItem dependency) {
        return dependency.elementName() + " " + dependency.elementId();
    }
}
