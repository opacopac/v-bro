package com.tschanz.v_bro.element_classes.presentation.swing.controller;

import com.tschanz.v_bro.app.presentation.swing.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.swing.viewmodel.StatusItem;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.element_classes.presentation.swing.view.ElementDenominationView;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.DenominationItem;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.ElementClassItem;
import com.tschanz.v_bro.element_classes.usecase.read_element_denominations.ReadElementDenominationsResponse;
import com.tschanz.v_bro.element_classes.usecase.read_element_denominations.ReadElementDenominationsUseCase;
import com.tschanz.v_bro.repo.presentation.swing.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class ElementDenominationController {
    private final ElementDenominationView elementDenominationView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final BehaviorSubject<List<DenominationItem>> denominationList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<List<DenominationItem>> selectDenominationsAction = new BehaviorSubject<>(Collections.emptyList());
    private final ReadElementDenominationsUseCase readElementNameFieldsUc;


    public BehaviorSubject<List<DenominationItem>> getSelectDenominationsAction() {
        return this.selectDenominationsAction;
    }

    public ElementDenominationController(
        ElementDenominationView elementDenominationView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        ReadElementDenominationsUseCase readElementNameFieldsUc
    ) {
        this.elementDenominationView = elementDenominationView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.readElementNameFieldsUc = readElementNameFieldsUc;

        this.elementDenominationView.bindDenominationList(this.denominationList);
        this.elementDenominationView.bindSelectDenominationsAction(this.selectDenominationsAction);

        this.selectElementClassAction.subscribe(new GenericSubscriber<>(this::onElementClassSelected));
    }


    private void onElementClassSelected(ElementClassItem selectedElementClass) {
        try {
            RepoConnectionItem repoConnection = this.repoConnection.getCurrentValue();
            this.updateDenominations(repoConnection, selectedElementClass);

            List<DenominationItem> selectedDenominations = this.selectDenominationsAction.getCurrentValue();
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void updateDenominations(
        RepoConnectionItem repoConnection,
        ElementClassItem selectedElementClass
    ) throws VBroAppException {
        List<DenominationItem> newDenominations;

        if (repoConnection == null || selectedElementClass == null) {
            newDenominations = Collections.emptyList();
        } else {
            ReadElementDenominationsResponse response = this.readElementNameFieldsUc.readDenominations(
                new OpenConnectionResponse.RepoConnection(repoConnection.repoType),
                selectedElementClass.getName()
            );

            newDenominations = response.denominations
                .stream()
                .map(nameField -> new DenominationItem(nameField.name))
                .collect(Collectors.toList());
        }

        this.denominationList.next(newDenominations);
    }
}
