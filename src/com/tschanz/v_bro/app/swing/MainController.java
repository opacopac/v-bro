package com.tschanz.v_bro.app.swing;

import com.tschanz.v_bro.elements.swing.element_selection.ElementSelectionController;
import com.tschanz.v_bro.elements.usecase.read_element_namefields.ReadElementNameFieldsUseCase;
import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.repo.swing.connection.ConnectionController;
import com.tschanz.v_bro.elements.swing.element_class_selection.ElementClassSelectionController;
import com.tschanz.v_bro.versioning.swing.versionfilter.VersionFilterController;
import com.tschanz.v_bro.versioning.swing.versions.VersionsController;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;


public class MainController {
    public MainController(
        MainView mainView,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc,
        ReadElementClassesUseCase readElementClassesUc,
        ReadElementNameFieldsUseCase readElementNameFieldsUc,
        ReadElementsUseCase readElementsUc,
        ReadVersionsUseCase readVersionsUc
    ) {
        ConnectionController connectionController = new ConnectionController(
            mainView.getConnectionView(),
            mainView.getElementClassSelectionView(),
            mainView.getStatusBarView(),
            openConnectionUc,
            readElementClassesUc,
            closeConnectionUc
        );

        new ElementClassSelectionController(
            mainView.getElementClassSelectionView(),
            mainView.getElementSelectionView(),
            mainView.getStatusBarView(),
            readElementNameFieldsUc,
            readElementsUc,
            connectionController
        );

        new ElementSelectionController(
            mainView.getElementSelectionView(),
            mainView.getElementClassSelectionView(),
            mainView.getVersionFilterView(),
            mainView.getVersionsView(),
            mainView.getStatusBarView(),
            readVersionsUc,
            connectionController
        );

        new VersionFilterController(
            mainView.getVersionFilterView()
        );

        new VersionsController(
            mainView.getVersionsView(),
            mainView.getElementClassSelectionView(),
            mainView.getDependencySelectionView(),
            mainView.getStatusBarView(),
            connectionController
        );
    }
}
