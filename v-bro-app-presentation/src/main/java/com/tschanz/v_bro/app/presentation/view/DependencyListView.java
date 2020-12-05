package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.actions.SelectDependencyVersionAction;

import java.util.List;
import java.util.concurrent.Flow;


public interface DependencyListView {
    void bindViewModel(
        Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList,
        Flow.Publisher<VersionFilterItem> versionFilter,
        SelectDependencyVersionAction selectDependencyVersionAction
    );
}
