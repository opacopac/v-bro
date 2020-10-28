package com.tschanz.v_bro.element_classes.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.StatusItem;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.element_classes.presentation.view.ElementClassView;
import com.tschanz.v_bro.element_classes.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.element_classes.usecase.read_element_classes.ReadElementClassesResponse;
import com.tschanz.v_bro.element_classes.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.repo.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class ElementClassController {
    private final ElementClassView elementClassView;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final BehaviorSubject<List<ElementClassItem>> elementClassList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<ElementClassItem> selectElementClassAction = new BehaviorSubject<>(null);
    private final ReadElementClassesUseCase readElementClassesUc;


    public BehaviorSubject<ElementClassItem> getSelectElementClassAction() { return selectElementClassAction; }


    public ElementClassController(
        ElementClassView elementClassView,
        BehaviorSubject<StatusItem> appStatus,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        ReadElementClassesUseCase readElementClassesUc
    ) {
        this.elementClassView = elementClassView;
        this.appStatus = appStatus;
        this.repoConnection = repoConnection;
        this.readElementClassesUc = readElementClassesUc;

        this.elementClassView.bindElementClassList(this.elementClassList);
        this.elementClassView.bindSelectElementClassAction(this.selectElementClassAction);

        this.repoConnection.subscribe(new GenericSubscriber<>(this::onConnectionChanged));
    }


    private void onConnectionChanged(RepoConnectionItem repoConnection) {
        try {
            this.updateElementClasses(repoConnection);
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
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
}
