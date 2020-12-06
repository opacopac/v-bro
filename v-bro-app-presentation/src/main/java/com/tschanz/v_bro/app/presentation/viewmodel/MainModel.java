package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class MainModel {
    public final BehaviorSubject<StatusItem> appStatus = new BehaviorSubject<>(null);
    public final BehaviorSubject<List<QuickConnectionItem>> quickConnectionList = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<RepoConnectionItem> currentRepoConnection = new BehaviorSubject<>(null);
    public final BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses = new BehaviorSubject<>(new SelectableItemList<>(Collections.emptyList(), null));
    public final BehaviorSubject<MultiSelectableItemList<DenominationItem>> elementDenominations = new BehaviorSubject<>(new MultiSelectableItemList<>(Collections.emptyList(), Collections.emptyList()));
    public final BehaviorSubject<SelectableItemList<ElementItem>> elements = new BehaviorSubject<>(new SelectableItemList<>(Collections.emptyList(), null));
    public final BehaviorSubject<SelectableItemList<VersionItem>> versions = new BehaviorSubject<>(new SelectableItemList<>(Collections.emptyList(), null));
    public final BehaviorSubject<VersionFilterItem> versionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), VersionData.HIGH_DATE, VersionData.DEFAULT_PFLEGESTATUS));
    public final BehaviorSubject<VersionFilterItem> effectiveVersionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), LocalDate.of(2025, 1, 1), VersionData.DEFAULT_PFLEGESTATUS));
    public final BehaviorSubject<DependencyFilterItem> dependencyFilter = new BehaviorSubject<>(new DependencyFilterItem(true));
    public final BehaviorSubject<List<FwdDependencyItem>> fwdDependencies = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<VersionAggregateItem> versionAggregate = new BehaviorSubject<>(null);
    public Long lastElementQueryTimestamp = 0L;
}
