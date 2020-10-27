package com.tschanz.v_bro.version_aggregates.presentation.swing.controller;

import com.tschanz.v_bro.app.presentation.swing.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.swing.viewmodel.StatusItem;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.ElementClassItem;
import com.tschanz.v_bro.elements.presentation.swing.viewmodel.ElementItem;
import com.tschanz.v_bro.repo.presentation.swing.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.version_aggregates.presentation.swing.view.VersionAggregateView;
import com.tschanz.v_bro.version_aggregates.presentation.swing.viewmodel.AggregateNodeItem;
import com.tschanz.v_bro.version_aggregates.presentation.swing.viewmodel.FieldAggregateNodeItem;
import com.tschanz.v_bro.version_aggregates.presentation.swing.viewmodel.VersionAggregateItem;
import com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate.ReadVersionAggregateResponse;
import com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate.ReadVersionAggregateUseCase;
import com.tschanz.v_bro.versions.presentation.swing.viewmodel.VersionItem;

import java.util.stream.Collectors;


public class VersionAggregateController {
    private final VersionAggregateView versionAggregateView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final BehaviorSubject<ElementItem> selectElementAction;
    private final BehaviorSubject<VersionItem> selectVersionAction;
    private final BehaviorSubject<VersionAggregateItem> versionAggregate = new BehaviorSubject<>(null);
    private final ReadVersionAggregateUseCase readVersionAggregateUc;


    public VersionAggregateController(
        VersionAggregateView versionAggregateView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        BehaviorSubject<ElementItem> selectElementAction,
        BehaviorSubject<VersionItem> selectVersionAction,
        ReadVersionAggregateUseCase readVersionAggregateUc
    ) {
        this.versionAggregateView = versionAggregateView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementAction = selectElementAction;
        this.selectVersionAction = selectVersionAction;
        this.readVersionAggregateUc = readVersionAggregateUc;

        this.selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));

        this.versionAggregateView.bindVersionAggregate(this.versionAggregate);
    }


    private void onVersionSelected(VersionItem selectedVersion) {
        if (selectedVersion == null) {
            return;
        }

        try {
            this.updateVersionAggregate(selectedVersion);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void updateVersionAggregate(VersionItem selectedVersion) throws VBroAppException {
        ReadVersionAggregateResponse response = this.readVersionAggregateUc.readVersionAggregate(
            new OpenConnectionResponse.RepoConnection(this.repoConnection.getCurrentValue().getRepoType()),
            this.selectElementClassAction.getCurrentValue().getName(),
            this.selectElementAction.getCurrentValue().getId(),
            selectedVersion.getId()
        );

        VersionAggregateItem versionAggregate = new VersionAggregateItem(
            this.createAggregateNodeItem(response.versionAggregateInfo.rootNode)
        );

        this.versionAggregate.next(versionAggregate);
    }


    private AggregateNodeItem createAggregateNodeItem(ReadVersionAggregateResponse.AggregateNodeInfo aggregateNodeInfo) {
        return new AggregateNodeItem(
            aggregateNodeInfo.nodeName,
            aggregateNodeInfo.fieldValues
                .stream()
                .map(field -> new FieldAggregateNodeItem(field.key, field.value))
                .collect(Collectors.toList()),
            aggregateNodeInfo.childNodes
                .stream()
                .map(this::createAggregateNodeItem)
                .collect(Collectors.toList())
        );
    }
}
