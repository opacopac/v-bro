package com.tschanz.v_bro.versioning.swing.controller;

import com.tschanz.v_bro.app.swing.model.ErrorStatusItem;
import com.tschanz.v_bro.app.swing.model.StatusItem;
import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.GenericSubscriber;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.VersionFilter;
import com.tschanz.v_bro.repo.swing.model.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.versioning.swing.view.VersionsView;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.versioning.swing.model.VersionItem;
import com.tschanz.v_bro.versioning.usecase.read_version_timeline.ReadVersionTimelineResponse;
import com.tschanz.v_bro.versioning.usecase.read_version_timeline.ReadVersionTimelineUseCase;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class VersionsController {
    private final VersionsView versionsView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectedElementClass;
    private final BehaviorSubject<ElementItem> selectedElement;
    private final BehaviorSubject<VersionFilter> selectedVersionFilter;
    private final BehaviorSubject<List<VersionItem>> versionList = new BehaviorSubject<>(Collections.emptyList());
    private final ReadVersionTimelineUseCase readVersionsUc;


    public VersionsController(
        VersionsView versionsView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectedElementClass,
        BehaviorSubject<ElementItem> selectedElement,
        BehaviorSubject<VersionFilter> selectedVersionFilter,
        ReadVersionTimelineUseCase readVersionsUc
    ) {
        this.versionsView = versionsView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.selectedElementClass = selectedElementClass;
        this.selectedElement = selectedElement;
        this.selectedVersionFilter = selectedVersionFilter;
        this.readVersionsUc = readVersionsUc;

        this.selectedElement.subscribe(new GenericSubscriber<>(this::onElementSelected));

        this.versionsView.addSelectVersionListener(this::onVersionSelected);

        new VersionTimelineController(
            this.versionsView.getVersionTimelineView()
        );
    }


    private void onElementSelected(ElementItem elementItem) {
        try {
            this.updateVersions(
                this.repoConnection.getCurrentValue(),
                this.selectedElementClass.getCurrentValue(),
                elementItem,
                this.selectedVersionFilter.getCurrentValue()
            );
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void onVersionSelected(ActionEvent e) {
        // TODO
    }


    private void updateVersions(
        RepoConnectionItem repoConnection,
        ElementClassItem selectedElementClass,
        ElementItem selectedElement,
        VersionFilter selectedVersionFilter
    ) throws VBroAppException {
        List<VersionItem> newVersions;

        if (repoConnection == null || selectedElementClass == null || selectedElement == null || selectedVersionFilter == null) {
            newVersions = Collections.emptyList();
        } else {
            ReadVersionTimelineResponse response = this.readVersionsUc.readVersionTimeline(
                new OpenConnectionResponse.RepoConnection(repoConnection.repoType),
                selectedElementClass.getName(),
                selectedElement.getId(),
                selectedVersionFilter.getMinGueltiVon(),
                selectedVersionFilter.getMaxGueltigBis(),
                selectedVersionFilter.getMinPflegestatus()
            );

            newVersions = response.versionInfoEntries
                .stream()
                .map(versionInfo -> new VersionItem(versionInfo.id, versionInfo.gueltigVon, versionInfo.gueltigBis))
                .collect(Collectors.toList());
        }

        this.versionList.next(newVersions);
    }
}
