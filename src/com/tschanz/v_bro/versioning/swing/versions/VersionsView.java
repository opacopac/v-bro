package com.tschanz.v_bro.versioning.swing.versions;

import com.tschanz.v_bro.versioning.swing.versiontimeline.VersionTimelineView;
import com.tschanz.v_bro.elements.swing.model.VersionItem;

import java.awt.event.ActionListener;
import java.util.List;


public interface VersionsView {
    void addSelectVersionListener(ActionListener listener);

    VersionTimelineView getVersionTimelineView();

    VersionItem getSelectedVersion();

    void setVersionList(List<VersionItem> versionItems);
}
