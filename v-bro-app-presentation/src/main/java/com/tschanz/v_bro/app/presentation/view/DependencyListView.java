package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface DependencyListView {
    void bindViewModel(
        Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList,
        Flow.Publisher<VersionFilterItem> versionFilter,
        ViewAction<ElementVersionVector> selectDependencyVersionAction
    );
}
