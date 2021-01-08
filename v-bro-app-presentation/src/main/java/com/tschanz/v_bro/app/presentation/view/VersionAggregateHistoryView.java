package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.VersionAggregateHistoryController;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate_history.VersionAggregateHistoryItem;

import java.util.concurrent.Flow;


public interface VersionAggregateHistoryView {
    void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementClassItem>> elementClasses,
        Flow.Publisher<ElementItem> currentElement,
        Flow.Publisher<SelectableItemList<VersionItem>> versions,
        Flow.Publisher<VersionAggregateHistoryItem> versionAggregateHistory,
        VersionAggregateHistoryController versionAggregateHistoryController
    );
}
