package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;

import java.util.concurrent.Flow;


public interface VersionFilterView {
    void bindInitialVersionFilter(Flow.Publisher<VersionFilterItem> versionFilter);

    void bindSelectVersionFilterAction(BehaviorSubject<VersionFilterItem> selectVersionFilterAction);
}
