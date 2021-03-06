package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyListController;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface DependencyListView {
    void bindViewModel(
        Flow.Publisher<List<DependencyItem>> fwdDependencyList,
        Flow.Publisher<VersionFilterItem> versionFilter,
        DependencyListController dependencyListController
    );
}
