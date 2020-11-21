package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.*;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class MainModel {
    public final BehaviorSubject<StatusItem> appStatus = new BehaviorSubject<>(null);

    public final BehaviorSubject<List<QuickConnectionItem>> quickConnectionList = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<RepoConnectionItem> currentRepoConnection = new BehaviorSubject<>(null);
    public final ConnectToRepoAction connectToRepoAction = new ConnectToRepoAction(null);

    public final BehaviorSubject<SelectedItemList<ElementClassItem>> elementClasses = new BehaviorSubject<>(new SelectedItemList<>(Collections.emptyList(), null));
    public final SelectElementClassAction selectElementClassAction = new SelectElementClassAction(null);

    public final BehaviorSubject<MultiSelectedItemList<DenominationItem>> elementDenominations = new BehaviorSubject<>(new MultiSelectedItemList<>(Collections.emptyList(), Collections.emptyList()));
    public final SelectDenominationsAction selectDenominationsAction = new SelectDenominationsAction(Collections.emptyList());

    public final BehaviorSubject<SelectedItemList<ElementItem>> elements = new BehaviorSubject<>(new SelectedItemList<>(Collections.emptyList(), null));
    public final SelectElementAction selectElementAction = new SelectElementAction(null);

    public final BehaviorSubject<SelectedItemList<VersionItem>> versions = new BehaviorSubject<>(new SelectedItemList<>(Collections.emptyList(), null));
    public final SelectVersionAction selectVersionAction = new SelectVersionAction(null);

    public final BehaviorSubject<VersionFilterItem> versionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), VersionData.HIGH_DATE, VersionData.DEFAULT_PFLEGESTATUS));
    public final SelectVersionFilterAction selectVersionFilterAction = new SelectVersionFilterAction(versionFilter.getCurrentValue());
    public final BehaviorSubject<VersionFilterItem> effectiveVersionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), LocalDate.of(2025, 1, 1), VersionData.DEFAULT_PFLEGESTATUS));

    public final BehaviorSubject<DependencyFilterItem> dependencyFilter = new BehaviorSubject<>(new DependencyFilterItem(true));
    public final SelectDependencyFilterAction selectDependencyFilterAction = new SelectDependencyFilterAction(new DependencyFilterItem(true));
    public final BehaviorSubject<List<FwdDependencyItem>> fwdDependencies = new BehaviorSubject<>(Collections.emptyList());
    public final SelectDependencyVersionAction selectDependencyVersionAction = new SelectDependencyVersionAction(null);

    public final BehaviorSubject<VersionAggregateItem> versionAggregate = new BehaviorSubject<>(null);
}
