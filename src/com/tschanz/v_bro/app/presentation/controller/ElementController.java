package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionFilterAction;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;


public class ElementController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final SelectElementClassAction selectElementClassAction;
    private final SelectVersionFilterAction selectVersionFilterAction;
    private final SelectElementUseCase selectElementUc;


    public ElementController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        SelectElementClassAction selectElementClassAction,
        SelectElementAction selectElementAction,
        SelectVersionFilterAction selectVersionFilterAction,
        SelectElementUseCase selectElementUc
    ) {
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectVersionFilterAction = selectVersionFilterAction;
        this.selectElementUc = selectElementUc;

        selectElementAction.subscribe(new GenericSubscriber<>(this::onElementSelected));
    }


    private void onElementSelected(String selectedElementId) {
        if (this.repoConnection.getCurrentValue() == null
            || this.selectElementClassAction.getCurrentValue() == null
            || selectedElementId == null
            || this.selectVersionFilterAction.getCurrentValue() == null
        ) {
            return;
        }

        SelectElementRequest request = new SelectElementRequest(
            this.repoConnection.getCurrentValue().repoType,
            this.selectElementClassAction.getCurrentValue(),
            selectedElementId,
            this.getVersionFilterRequest(this.selectVersionFilterAction.getCurrentValue())
        );
        this.selectElementUc.execute(request);
    }


    private VersionFilterRequest getVersionFilterRequest(VersionFilterItem versionFilter) {
        return new VersionFilterRequest(
            versionFilter.getMinGueltigVon(),
            versionFilter.getMaxGueltigBis(),
            versionFilter.getMinPflegestatus()
        );
    }
}
