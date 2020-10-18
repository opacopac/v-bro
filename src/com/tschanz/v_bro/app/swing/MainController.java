package com.tschanz.v_bro.app.swing;

import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.repo.swing.connection.ConnectionController;
import com.tschanz.v_bro.elements.swing.elementselection.ElementSelectionController;
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
        ReadElementsUseCase readElementsUc,
        ReadVersionsUseCase readVersionsUc
    ) {
        ConnectionController connectionController = new ConnectionController(
            mainView.getConnectionView(),
            mainView.getElementSelectionView(),
            mainView.getStatusBarView(),
            openConnectionUc,
            readElementClassesUc,
            closeConnectionUc
        );

        new ElementSelectionController(
            mainView.getElementSelectionView(),
            mainView.getVersionFilterView(),
            mainView.getVersionsView(),
            mainView.getDependencySelectionView(),
            mainView.getStatusBarView(),
            readElementsUc,
            readVersionsUc,
            connectionController
        );

        new VersionFilterController(
            mainView.getVersionFilterView()
        );

        new VersionsController(
            mainView.getVersionsView(),
            mainView.getElementSelectionView(),
            mainView.getDependencySelectionView(),
            mainView.getStatusBarView(),
            connectionController
        );
    }
}
