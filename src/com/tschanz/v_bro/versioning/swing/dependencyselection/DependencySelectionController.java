package com.tschanz.v_bro.versioning.swing.dependencyselection;

import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;

import java.awt.event.ActionEvent;


public class DependencySelectionController {
    private final DependencySelectionView dependencySelectionView;
    private final StatusBarView statusBarView;


    public DependencySelectionController(
        DependencySelectionView dependencySelectionView,
        StatusBarView statusBarView
    ) {
        this.dependencySelectionView = dependencySelectionView;
        this.statusBarView = statusBarView;

        this.dependencySelectionView.addSelectElementTableListener(this::onElementTableSelected);
    }


    private void onElementTableSelected(ActionEvent e) {
        /*try {
            String selectedElementTableName = this.findFwDependencyTablesUc.findFwDependencTables();
            if (selectedElementTableName == null || selectedElementTableName.length() == 0) {
                return;
            }
            ReadElementsResponse readElementsResponse = this.readElementsUc.readElements(selectedElementTableName);
            this.elementSelectionView.setElements(readElementsResponse.elements);
        } catch (ZomfiAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }*/
    }
}
