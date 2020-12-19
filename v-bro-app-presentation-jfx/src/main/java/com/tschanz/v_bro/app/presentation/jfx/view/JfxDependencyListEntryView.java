package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyListController;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Collections;
import java.util.concurrent.Flow;


public class JfxDependencyListEntryView {
    private final BehaviorSubject<SelectableItemList<VersionItem>> versionList = new BehaviorSubject<>(new SelectableItemList<>(Collections.emptyList(), null)); // TODO
    private DependencyListController dependencyListController;
    private FwdDependencyItem fwdDependencyItem;
    @FXML private Label dependencyName;
    @FXML private JfxVersionView versionViewController;


    public void bindViewModel(
        FwdDependencyItem fwdDependency,
        Flow.Publisher<VersionFilterItem> versionFilter,
        DependencyListController dependencyListController
    ) {
        this.fwdDependencyItem = fwdDependency;
        this.dependencyListController = dependencyListController;
        this.dependencyName.setText(this.createDependencyName(fwdDependency));
        this.versionList.next(new SelectableItemList<>(fwdDependency.getVersions(), null)); // TODO

        this.versionViewController.bindViewModel(this.versionList, versionFilter, this::onDependencyVersionSelected);
    }


    private String createDependencyName(FwdDependencyItem dependency) {
        return dependency.getElementClass() + " - " + dependency.getElementId();
    }


    private void onDependencyVersionSelected(String versionId) {
        if (this.dependencyListController == null
            || this.fwdDependencyItem.getElementClass() == null
            || this.fwdDependencyItem.getElementId() == null
            || versionId == null
        ) {
            return;
        }

        ElementVersionVector dependencyVersion = new ElementVersionVector(
            this.fwdDependencyItem.getElementClass(),
            this.fwdDependencyItem.getElementId(),
            versionId
        );

        new Thread(() -> this.dependencyListController.onVersionFilterSelected(dependencyVersion)).start();
    }
}
