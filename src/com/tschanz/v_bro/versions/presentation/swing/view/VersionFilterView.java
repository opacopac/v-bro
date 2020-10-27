package com.tschanz.v_bro.versions.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.elements.presentation.swing.viewmodel.VersionFilter;

import java.util.concurrent.Flow;


public interface VersionFilterView {
    void bindVersionFilter(Flow.Publisher<VersionFilter> versionFilter);

    void bindSelectVersionFilterAction(BehaviorSubject<VersionFilter> selectVersionFilterAction);
}
