package com.tschanz.v_bro.versions.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.StatusItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.presentation.viewmodel.ElementItem;
import com.tschanz.v_bro.elements.presentation.viewmodel.VersionFilter;
import com.tschanz.v_bro.repo.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.versions.presentation.view.VersionsView;
import com.tschanz.v_bro.element_classes.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.versions.presentation.viewmodel.VersionItem;
import com.tschanz.v_bro.versions.usecase.read_version_timeline.ReadVersionTimelineResponse;
import com.tschanz.v_bro.versions.usecase.read_version_timeline.ReadVersionTimelineUseCase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class VersionController {
    private final VersionsView versionsView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final BehaviorSubject<ElementItem> selectElementAction;
    private final BehaviorSubject<VersionFilter> selectVersionFilterAction;
    private final BehaviorSubject<List<VersionItem>> versionList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<VersionItem> selectVersionAction = new BehaviorSubject<>(null);
    private final ReadVersionTimelineUseCase readVersionsUc;


    public VersionController(
        VersionsView versionsView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        BehaviorSubject<ElementItem> selectElementAction,
        BehaviorSubject<VersionFilter> selectVersionFilterAction,
        ReadVersionTimelineUseCase readVersionsUc
    ) {
        this.versionsView = versionsView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementAction = selectElementAction;
        this.selectVersionFilterAction = selectVersionFilterAction;
        this.readVersionsUc = readVersionsUc;

        this.selectElementAction.subscribe(new GenericSubscriber<>(this::onElementSelected));
        this.selectVersionFilterAction.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));

        this.versionsView.bindVersionList(this.versionList);
        this.versionsView.bindSelectVersionAction(this.selectVersionAction);
    }


    public BehaviorSubject<VersionItem> getSelectVersionAction() {
        return this.selectVersionAction;
    }


    private void onElementSelected(ElementItem elementItem) {
        try {
            this.updateVersions(
                this.repoConnection.getCurrentValue(),
                this.selectElementClassAction.getCurrentValue(),
                elementItem,
                this.selectVersionFilterAction.getCurrentValue()
            );
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void onVersionFilterSelected(VersionFilter selectedVersionFilter) {
        try {
            this.updateVersions(
                this.repoConnection.getCurrentValue(),
                this.selectElementClassAction.getCurrentValue(),
                this.selectElementAction.getCurrentValue(),
                selectedVersionFilter
            );
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
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
