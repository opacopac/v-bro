package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface VersionsView {
    void bindVersionList(Flow.Publisher<List<VersionItem>> versionList);

    void bindEffectiveVersionFilter(Flow.Publisher<VersionFilterItem> effectiveVersionFilter);

    void bindSelectVersionAction(BehaviorSubject<VersionItem> selectVersionAction);
}
