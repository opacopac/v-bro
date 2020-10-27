package com.tschanz.v_bro.elements.presentation.swing.controller;

import com.tschanz.v_bro.app.presentation.swing.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.swing.viewmodel.StatusItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.DenominationItem;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.ElementClassItem;
import com.tschanz.v_bro.elements.presentation.swing.view.ElementView;
import com.tschanz.v_bro.elements.presentation.swing.viewmodel.ElementItem;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsResponse;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.repo.presentation.swing.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class ElementController {
    private final ElementView elementView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<List<ElementItem>> elementList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<ElementClassItem> selectElementClassAction;
    private final BehaviorSubject<List<DenominationItem>> selectDenominationsAction;
    private final BehaviorSubject<ElementItem> selectElementAction = new BehaviorSubject<>(null);
    private final ReadElementsUseCase readElementsUc;


    public BehaviorSubject<ElementItem> getSelectElementAction() { return selectElementAction; }


    public ElementController(
        ElementView elementView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        BehaviorSubject<ElementClassItem> selectElementClassAction,
        BehaviorSubject<List<DenominationItem>> selectDenominationsAction,
        ReadElementsUseCase readElementsUc
    ) {
        this.elementView = elementView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.selectElementClassAction = selectElementClassAction;
        this.selectDenominationsAction = selectDenominationsAction;
        this.readElementsUc = readElementsUc;

        this.elementView.bindElementList(this.elementList);
        this.elementView.bindSelectElementAction(this.selectElementAction);

        this.selectElementClassAction.subscribe(new GenericSubscriber<>(this::onElementClassSelected));
        this.selectDenominationsAction.subscribe(new GenericSubscriber<>(this::onDenominationsSelected));
    }


    private void onElementClassSelected(ElementClassItem selectedElementClass) {
        try {
            RepoConnectionItem repoConnection = this.repoConnection.getCurrentValue();
            List<DenominationItem> selectedDenominations = this.selectDenominationsAction.getCurrentValue();
            this.updateElements(repoConnection, selectedElementClass, selectedDenominations);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void onDenominationsSelected(List<DenominationItem> selectedDenominations) {
        try {
            RepoConnectionItem repoConnection = this.repoConnection.getCurrentValue();
            ElementClassItem selectedElementClass = this.selectElementClassAction.getCurrentValue();
            this.updateElements(repoConnection, selectedElementClass, selectedDenominations);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void updateElements(
        RepoConnectionItem repoConnection,
        ElementClassItem selectedElementClass,
        List<DenominationItem> selectedDenominations
    ) throws VBroAppException {
        List<ElementItem> newElements;

        if (repoConnection == null || selectedElementClass == null || selectedDenominations == null) {
            newElements = Collections.emptyList();
        } else {
            ReadElementsResponse response = this.readElementsUc.readElements(
                new OpenConnectionResponse.RepoConnection(repoConnection.repoType),
                selectedElementClass.getName(),
                selectedDenominations
                    .stream()
                    .map(DenominationItem::getName)
                    .collect(Collectors.toList())
            );

            newElements = response.elements
                .stream()
                .map(element -> new ElementItem(element.id, element.name))
                .collect(Collectors.toList());
        }

        this.elementList.next(newElements);
    }
}
