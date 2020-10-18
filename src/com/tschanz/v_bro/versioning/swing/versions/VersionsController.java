package com.tschanz.v_bro.versioning.swing.versions;

import com.tschanz.v_bro.repo.swing.connection.ConnectionController;
import com.tschanz.v_bro.versioning.swing.dependencyselection.DependencySelectionView;
import com.tschanz.v_bro.elements.swing.element_class_selection.ElementClassSelectionView;
import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;
import com.tschanz.v_bro.versioning.swing.versiontimeline.VersionTimelineController;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.VersionItem;

import java.awt.event.ActionEvent;


public class VersionsController {
    private final VersionsView versionsView;
    private final ElementClassSelectionView elementClassSelectionView;
    private final DependencySelectionView dependencySelectionView;
    private final StatusBarView statusBarView;
    private final ConnectionController connectionController;


    public VersionsController(
        VersionsView versionsView,
        ElementClassSelectionView elementClassSelectionView,
        DependencySelectionView dependencySelectionView,
        StatusBarView statusBarView,
        ConnectionController connectionController
    ) {
        this.versionsView = versionsView;
        this.elementClassSelectionView = elementClassSelectionView;
        this.dependencySelectionView = dependencySelectionView;
        this.statusBarView = statusBarView;
        this.connectionController = connectionController;

        this.versionsView.addSelectVersionListener(this::onVersionSelected);

        new VersionTimelineController(
            this.versionsView.getVersionTimelineView()
        );
    }


    private void onVersionSelected(ActionEvent e) {
        ElementClassItem fwdElementTable = this.elementClassSelectionView.getSelectedElementClass();
        VersionItem version = this.versionsView.getSelectedVersion();
        if (fwdElementTable == null || version == null) {
            return;
        }

        /* try {
            // TODO
        } catch (ZomfiAppException exception) {
            VersionsController.this.statusBarView.setStatusError(exception.getMessage());
        } */
    }
}
