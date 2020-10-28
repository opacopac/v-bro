package com.tschanz.v_bro.dependencies.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.dependencies.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.versions.presentation.view.swing.VersionTimeline;
import com.tschanz.v_bro.versions.presentation.viewmodel.VersionItem;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;


public class DependencyPanel extends JPanel {
    private JLabel dependencyName = new JLabel("");
    private VersionTimeline versionTimeline = new VersionTimeline();
    private BehaviorSubject<java.util.List<VersionItem>> versionList = new BehaviorSubject<>(Collections.emptyList());


    public DependencyPanel() {
        //this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(dependencyName);
        this.add(this.versionTimeline);
        this.versionTimeline.bindVersionList(this.versionList);
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
