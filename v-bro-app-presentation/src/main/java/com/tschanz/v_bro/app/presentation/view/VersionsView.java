package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.presentation.actions.SelectVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;

import java.util.concurrent.Flow;


public interface VersionsView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<VersionItem>> versionList,
        Flow.Publisher<VersionFilterItem> effectiveVersionFilter,
        SelectVersionAction selectVersionAction
    );
}
