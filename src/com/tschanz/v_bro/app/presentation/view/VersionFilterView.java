package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionFilterAction;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;

import java.util.concurrent.Flow;


public interface VersionFilterView {
    void bindViewModel(
        Flow.Publisher<VersionFilterItem> versionFilter,
        SelectVersionFilterAction selectVersionFilterAction
    );
}
