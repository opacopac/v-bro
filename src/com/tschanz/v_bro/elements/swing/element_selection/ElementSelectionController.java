package com.tschanz.v_bro.elements.swing.element_selection;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;
import com.tschanz.v_bro.elements.swing.element_class_selection.ElementClassSelectionView;
import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.VersionItem;
import com.tschanz.v_bro.repo.swing.connection.ConnectionController;
import com.tschanz.v_bro.versioning.swing.versionfilter.VersionFilterView;
import com.tschanz.v_bro.versioning.swing.versions.VersionsView;
import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsResponse;
import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsUseCase;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;


public class ElementSelectionController {
    private final ElementSelectionView elementSelectionView;
    private final ElementClassSelectionView elementClassSelectionView;
    private final VersionFilterView versionFilterView;
    private final VersionsView versionsView;
    private final StatusBarView statusBarView;
    private final ReadVersionsUseCase readVersionsUc;
    private final ConnectionController connectionController;


    public ElementSelectionController(
        ElementSelectionView elementSelectionView,
        ElementClassSelectionView elementClassSelectionView,
        VersionFilterView versionFilterView,
        VersionsView versionsView,
        StatusBarView statusBarView,
        ReadVersionsUseCase readVersionsUc,
        ConnectionController connectionController
    ) {
        this.elementSelectionView = elementSelectionView;
        this.elementClassSelectionView = elementClassSelectionView;
        this.versionFilterView = versionFilterView;
        this.versionsView = versionsView;
        this.statusBarView = statusBarView;
        this.readVersionsUc = readVersionsUc;
        this.connectionController = connectionController;

        this.elementSelectionView.addSelectElementListener(this::onElementSelected);
    }


    private void onElementSelected(ActionEvent e) {
        try {
            ElementItem selectedElement = this.elementSelectionView.getSelectedElement();
            if (selectedElement == null) {
                return;
            }

            // read versions
            ReadVersionsResponse response = this.readVersionsUc.readVersions(
                this.connectionController.getCurrentConnection(),
                this.elementClassSelectionView.getSelectedElementClass().getName(),
                selectedElement.getId(),
                this.versionFilterView.getMinVonDate(),
                this.versionFilterView.getMaxBisDate()
            );
            List<VersionItem> versionItems = response.versions
                .stream()
                .map(version -> new VersionItem(version.id, version.gueltigVon, version.gueltigBis))
                .collect(Collectors.toList());
            this.versionsView.setVersionList(versionItems);
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }
    }
}
