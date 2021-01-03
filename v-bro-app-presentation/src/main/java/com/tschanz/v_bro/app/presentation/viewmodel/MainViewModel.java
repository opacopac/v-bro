package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.QuickConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.status.StatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate.VersionAggregateItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class MainViewModel {
    public final BehaviorSubject<StatusItem> appStatus = new BehaviorSubject<>(null);
    public final BehaviorSubject<List<QuickConnectionItem>> quickConnectionList = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<RepoConnectionItem> currentRepoConnection = new BehaviorSubject<>(null);
    public final BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses = new BehaviorSubject<>(new SelectableItemList<>(Collections.emptyList(), null));
    public final BehaviorSubject<MultiSelectableItemList<DenominationItem>> elementDenominations = new BehaviorSubject<>(new MultiSelectableItemList<>(Collections.emptyList(), Collections.emptyList()));
    public final BehaviorSubject<ElementItem> currentElement = new BehaviorSubject<>(null);
    public final BehaviorSubject<SelectableItemList<VersionItem>> versions = new BehaviorSubject<>(new SelectableItemList<>(Collections.emptyList(), null));
    public final BehaviorSubject<VersionFilterItem> versionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), VersionData.HIGH_DATE, Pflegestatus.IN_ARBEIT));
    public final BehaviorSubject<VersionFilterItem> effectiveVersionFilter = new BehaviorSubject<>(new VersionFilterItem(LocalDate.of(2015, 1, 1), LocalDate.of(2025, 1, 1), Pflegestatus.IN_ARBEIT));
    public final BehaviorSubject<DependencyFilterItem> dependencyFilter = new BehaviorSubject<>(new DependencyFilterItem(true));
    public final BehaviorSubject<List<FwdDependencyItem>> fwdDependencies = new BehaviorSubject<>(Collections.emptyList());
    public final BehaviorSubject<VersionAggregateItem> versionAggregate = new BehaviorSubject<>(null);
}
