package com.tschanz.v_bro.versioning.swing.versions;

import com.tschanz.v_bro.versioning.swing.versiontimeline.VersionTimeline;
import com.tschanz.v_bro.versioning.swing.versiontimeline.VersionTimelineView;
import com.tschanz.v_bro.elements.swing.model.VersionItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class VersionsPanel extends JPanel implements VersionsView {
    private final VersionTimeline timeline = new VersionTimeline();


    public VersionsPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Versions"));
        this.add(this.timeline);
    }


    @Override
    public VersionTimelineView getVersionTimelineView() {
        return this.timeline;
    }


    @Override
    public void setVersionList(List<VersionItem> versionList) {
        this.timeline.setVersionList(versionList);
    }


    @Override
    public VersionItem getSelectedVersion() {
        return this.timeline.getSelectedVersion();
    }


    @Override
    public void addSelectVersionListener(ActionListener listener) {
        this.timeline.addVersionSelectedListener(listener);
    }
}
