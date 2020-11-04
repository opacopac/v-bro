package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface DependenciesView {
    void bindEffectiveVersionFilter(Flow.Publisher<VersionFilterItem> versionFilter);

    void bindFwdDependencyList(Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList);
}
