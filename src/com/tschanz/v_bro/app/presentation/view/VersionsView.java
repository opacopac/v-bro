package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface VersionsView {
    void bindViewModel(
        Flow.Publisher<List<VersionItem>> versionList,
        Flow.Publisher<VersionFilterItem> effectiveVersionFilter,
        SelectVersionAction selectVersionAction
    );
}
