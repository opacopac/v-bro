package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.VersionFilterController;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;

import java.util.concurrent.Flow;


public interface VersionFilterView {
    void bindViewModel(
        Flow.Publisher<VersionFilterItem> versionFilter,
        VersionFilterController versionFilterController
    );
}
