package com.tschanz.v_bro.elements.swing.controller;

import com.tschanz.v_bro.app.swing.model.ErrorStatusItem;
import com.tschanz.v_bro.app.swing.model.StatusItem;
import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.GenericSubscriber;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.swing.model.DenominationItem;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.VersionFilter;
import com.tschanz.v_bro.elements.swing.view.ElementView;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesResponse;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.elements.usecase.read_element_denominations.ReadElementDenominationsResponse;
import com.tschanz.v_bro.elements.usecase.read_element_denominations.ReadElementDenominationsUseCase;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsResponse;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.repo.swing.model.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.versioning.domain.model.Pflegestatus;
import com.tschanz.v_bro.versioning.domain.model.VersionInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class ElementController {
    private final ElementView elementView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<List<ElementClassItem>> elementClassList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<List<DenominationItem>> denominationList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<List<ElementItem>> elementList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<ElementClassItem> selectedElementClass = new BehaviorSubject<>(null);
    private final BehaviorSubject<List<DenominationItem>> selectedDenominations = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<ElementItem> selectedElement = new BehaviorSubject<>(null);
    private final BehaviorSubject<VersionFilter> selectedVersionFilter = new BehaviorSubject<>(new VersionFilter(VersionInfo.LOW_DATE, VersionInfo.HIGH_DATE, Pflegestatus.TEST));
    private final ReadElementClassesUseCase readElementClassesUc;
    private final ReadElementDenominationsUseCase readElementNameFieldsUc;
    private final ReadElementsUseCase readElementsUc;


    public BehaviorSubject<ElementClassItem> getSelectedElementClass() { return selectedElementClass; }
    public BehaviorSubject<ElementItem> getSelectedElement() { return selectedElement; }
    public BehaviorSubject<VersionFilter> getSelectedVersionFilter() { return selectedVersionFilter; }


    public ElementController(
        ElementView elementView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        ReadElementClassesUseCase readElementClassesUc,
        ReadElementDenominationsUseCase readElementNameFieldsUc,
        ReadElementsUseCase readElementsUc
    ) {
        this.elementView = elementView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.readElementClassesUc = readElementClassesUc;
        this.readElementNameFieldsUc = readElementNameFieldsUc;
        this.readElementsUc = readElementsUc;

        this.elementView.bindElementClassList(this.elementClassList);
        this.elementView.bindDenominationList(this.denominationList);
        this.elementView.bindElementList(this.elementList);
        this.elementView.bindSelectElementClassAction(this.selectedElementClass);
        this.elementView.bindSelectDenominationsAction(this.selectedDenominations);
        this.elementView.bindSelectElementAction(this.selectedElement);
        this.elementView.bindVersionFilter(this.selectedVersionFilter);

        this.repoConnection.subscribe(new GenericSubscriber<>(this::onConnectionChanged));
        this.selectedElementClass.subscribe(new GenericSubscriber<>(this::onElementClassSelected));
        this.selectedDenominations.subscribe(new GenericSubscriber<>(this::onDenominationsSelected));
        this.selectedElement.subscribe(new GenericSubscriber<>(this::onElementSelected));
        this.selectedVersionFilter.subscribe(new GenericSubscriber<>(this::onVersionFilterSelected));
    }


    private void onConnectionChanged(RepoConnectionItem repoConnection) {
        try {
            this.updateElementClasses(repoConnection);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void onElementClassSelected(ElementClassItem selectedElementClass) {
        try {
            RepoConnectionItem repoConnection = this.repoConnection.getCurrentValue();
            this.updateDenominations(repoConnection, selectedElementClass);

            List<DenominationItem> selectedDenominations = this.selectedDenominations.getCurrentValue();
            this.updateElements(repoConnection, selectedElementClass, selectedDenominations);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void onDenominationsSelected(List<DenominationItem> selectedDenominations) {
        try {
            RepoConnectionItem repoConnection = this.repoConnection.getCurrentValue();
            ElementClassItem selectedElementClass = this.selectedElementClass.getCurrentValue();
            this.updateElements(repoConnection, selectedElementClass, selectedDenominations);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void onElementSelected(ElementItem selectedElement) {
        /*try {
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }*/
    }


    private void onVersionFilterSelected(VersionFilter selectedVersionFilter) {
        /*try {
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }*/
    }


    private void updateElementClasses(RepoConnectionItem repoConnection) throws VBroAppException {
        List<ElementClassItem> newElementClasses;

        if (repoConnection == null) {
            newElementClasses = Collections.emptyList();
        } else {
            ReadElementClassesResponse response = this.readElementClassesUc.readElementClasses(
                new OpenConnectionResponse.RepoConnection(repoConnection.repoType)
            );

            newElementClasses = response.elementTableNames
                .stream()
                .map(ElementClassItem::new)
                .collect(Collectors.toList());
        }

        this.elementClassList.next(newElementClasses);
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
