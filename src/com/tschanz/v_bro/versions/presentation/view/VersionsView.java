package com.tschanz.v_bro.versions.presentation.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.versions.presentation.viewmodel.VersionItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface VersionsView {
    void bindVersionList(Flow.Publisher<List<VersionItem>> versionList);

    void bindSelectVersionAction(BehaviorSubject<VersionItem> selectVersionAction);
}