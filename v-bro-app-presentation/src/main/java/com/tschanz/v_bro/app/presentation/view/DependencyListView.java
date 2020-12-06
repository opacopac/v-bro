package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface DependencyListView {
    void bindViewModel(
        Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList,
        Flow.Publisher<VersionFilterItem> versionFilter,
        ViewAction<ElementVersionVector> selectDependencyVersionAction
    );
}
