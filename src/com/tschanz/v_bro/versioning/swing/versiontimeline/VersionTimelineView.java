package com.tschanz.v_bro.versioning.swing.versiontimeline;

import com.tschanz.v_bro.elements.swing.model.VersionItem;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;


public interface VersionTimelineView {
    void addMouseListener(MouseListener listener);

    void addMouseMotionListener(MouseMotionListener listener);

    void addVersionSelectedListener(ActionListener listener);

    void setVersionList(List<VersionItem> versionList);

    VersionItem getHoverVersion();

    void setHoverVersion(VersionItem version);

    void setSelectedVersion(VersionItem version);

    VersionItem getSelectedVersion();

    VersionItem getMousPosVersion(int x, int y);
}
