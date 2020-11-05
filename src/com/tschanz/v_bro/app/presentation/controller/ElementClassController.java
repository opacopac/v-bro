package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.usecase.select_element_class.requestmodel.SelectElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassUseCase;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;


public class ElementClassController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final SelectElementClassUseCase selectElementClassUc;


    public ElementClassController(
        BehaviorSubject<RepoConnectionItem> repoConnection,
        SelectElementClassAction selectElementClassAction,
        SelectElementClassUseCase selectElementClassUc
    ) {
        this.repoConnection = repoConnection;
        this.selectElementClassUc = selectElementClassUc;
        selectElementClassAction.subscribe(new GenericSubscriber<>(this::onElementClassSelected));
    }


    private void onElementClassSelected(String selectedElementClass) {
        if (this.repoConnection.getCurrentValue() == null || selectedElementClass == null) {
            return;
        }

        SelectElementClassRequest request = new SelectElementClassRequest(
            this.repoConnection.getCurrentValue().repoType,
            selectedElementClass
        );
        this.selectElementClassUc.execute(request);
    }
}
