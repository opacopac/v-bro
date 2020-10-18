package com.tschanz.v_bro.elements.swing.elementselection;

import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.ElementTableItem;
import com.tschanz.v_bro.repo.swing.connection.ConnectionController;
import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsResponse;
import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsResponse;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.versioning.swing.dependencyselection.DependencySelectionView;
import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;
import com.tschanz.v_bro.versioning.swing.versions.VersionsView;
import com.tschanz.v_bro.versioning.swing.versionfilter.VersionFilterView;
import com.tschanz.v_bro.elements.swing.model.VersionItem;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class ElementSelectionController {
    private final ElementSelectionView elementSelectionView;
    private final VersionFilterView versionFilterView;
    private final VersionsView versionsView;
    private final DependencySelectionView dependencySelectionView;
    private final StatusBarView statusBarView;
    private final ReadElementsUseCase readElementsUc;
    private final ReadVersionsUseCase readVersionsUc;
    private final ConnectionController connectionController;


    public ElementSelectionController(
        ElementSelectionView elementSelectionView,
        VersionFilterView versionFilterView,
        VersionsView versionsView,
        DependencySelectionView dependencySelectionView,
        StatusBarView statusBarView,
        ReadElementsUseCase readElementsUc,
        ReadVersionsUseCase readVersionsUc,
        ConnectionController connectionController
    ) {
        this.elementSelectionView = elementSelectionView;
        this.versionFilterView = versionFilterView;
        this.versionsView = versionsView;
        this.dependencySelectionView = dependencySelectionView;
        this.statusBarView = statusBarView;
        this.readElementsUc = readElementsUc;
        this.readVersionsUc = readVersionsUc;
        this.connectionController = connectionController;

        this.elementSelectionView.addSelectElementTableListener(this::onElementTableSelected);
        this.elementSelectionView.addSelectElementListener(this::onElementSelected);
    }


    private void onElementTableSelected(ActionEvent e) {
        try {
            ElementTableItem selectedElementTable = this.elementSelectionView.getSelectedElementTable();
            if (selectedElementTable == null) {
                return;
            }

            // read elements
            ReadElementsResponse response = this.readElementsUc.readElements(
                this.connectionController.getCurrentConnection(),
                selectedElementTable.getName(),
                Collections.emptyList() // TODO: name fields
            );
            List<ElementItem> elementItems = response.elements
                .stream()
                .map(element -> new ElementItem(element.id, element.name))
                .collect(Collectors.toList());
            this.elementSelectionView.setElements(elementItems);

            // read fwd dependencies
            /*
            List<ElementTableItem> fwdElementItems = findFwDependencyTablesUc.findFwdDependentTables(selectedElementTable.getName())
                .fwdDepElementTableNames
                .stream()
                .map(ElementTableItem::new)
                .collect(Collectors.toList());
            this.dependencySelectionView.setElementTableNames(fwdElementItems);*/
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }
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
                this.elementSelectionView.getSelectedElementTable().getName(),
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
