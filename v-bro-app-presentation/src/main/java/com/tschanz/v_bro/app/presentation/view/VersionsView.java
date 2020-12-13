package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;

import java.util.concurrent.Flow;


public interface VersionsView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<VersionItem>> versionList,
        Flow.Publisher<VersionFilterItem> effectiveVersionFilter,
        ViewAction<String> selectVersionAction
    );
}
