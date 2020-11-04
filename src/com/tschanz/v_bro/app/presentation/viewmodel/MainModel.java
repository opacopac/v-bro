package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class MainModel {
    public final BehaviorSubject<StatusItem> appStatus = new BehaviorSubject<>(null);

    public final BehaviorSubject<List<QuickConnectionItem>> quickConnectionList = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<RepoConnectionItem> currentRepoConnection = new BehaviorSubject<>(null);
    public final BehaviorSubject<RepoConnectionItem> connectToRepoAction = new BehaviorSubject<>(null);

    public final BehaviorSubject<List<ElementClassItem>> elementClasses = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<ElementClassItem> selectedElementClass = new BehaviorSubject<>(null);

    public final BehaviorSubject<List<DenominationItem>> elementDenominations = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<List<DenominationItem>> selectedDenominations = new BehaviorSubject<>(Collections.emptyList());

    public final BehaviorSubject<List<ElementItem>> elements = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<ElementItem> selectedElement = new BehaviorSubject<>(null);

    public final BehaviorSubject<List<VersionItem>> versions = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<VersionItem> selectedVersion = new BehaviorSubject<>(null);

    public final BehaviorSubject<VersionFilterItem> versionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), VersionInfo.HIGH_DATE, VersionInfo.DEFAULT_PFLEGESTATUS));
    public final BehaviorSubject<VersionFilterItem> selectedVersionFilter = new BehaviorSubject<>(versionFilter.getCurrentValue());
    public final BehaviorSubject<VersionFilterItem> effectiveVersionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), LocalDate.of(2025, 1, 1), VersionInfo.DEFAULT_PFLEGESTATUS));

    public final BehaviorSubject<DependencyFilterItem> selectedDependencyFilter = new BehaviorSubject<>(new DependencyFilterItem(true));
    public final BehaviorSubject<List<FwdDependencyItem>> fwdDependencies = new BehaviorSubject<>(Collections.emptyList());

    public final BehaviorSubject<VersionAggregateItem> versionAggregate = new BehaviorSubject<>(null);
}
