package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.actions.SelectDependencyVersionAction;
import com.tschanz.v_bro.app.presentation.actions.SelectVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Collections;
import java.util.concurrent.Flow;


public class JfxDependencyListEntryView {
    private final BehaviorSubject<SelectableItemList<VersionItem>> versionList = new BehaviorSubject<>(new SelectableItemList<>(Collections.emptyList(), null)); // TODO
    private SelectDependencyVersionAction selectDependencyVersionAction;
    private FwdDependencyItem fwdDependencyItem;
    @FXML private Label dependencyName;
    @FXML private JfxVersionView versionViewController;


    public void bindViewModel(
        FwdDependencyItem fwdDependency,
        Flow.Publisher<VersionFilterItem> versionFilter,
        SelectDependencyVersionAction selectDependencyVersionAction
    ) {
        this.fwdDependencyItem = fwdDependency;
        this.selectDependencyVersionAction = selectDependencyVersionAction;
        this.dependencyName.setText(this.createDependencyName(fwdDependency));
        this.versionList.next(new SelectableItemList<>(fwdDependency.getVersions(), null)); // TODO

        SelectVersionAction selectVersionAction = new SelectVersionAction();
        selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));

        this.versionViewController.bindViewModel(this.versionList, versionFilter, selectVersionAction);
    }


    private String createDependencyName(FwdDependencyItem dependency) {
        return dependency.getElementClass() + " - " + dependency.getElementId();
    }


    private void onVersionSelected(String versionId) {
        if (this.selectDependencyVersionAction == null
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
        this.selectDependencyVersionAction.next(dependencyVersion);
    }
}
