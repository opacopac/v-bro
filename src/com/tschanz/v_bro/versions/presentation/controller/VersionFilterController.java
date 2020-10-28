package com.tschanz.v_bro.versions.presentation.controller;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.elements.presentation.viewmodel.VersionFilter;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.presentation.view.VersionFilterView;


public class VersionFilterController {
    private final VersionFilterView versionFilterView;
    private final BehaviorSubject<VersionFilter> versionFilter = new BehaviorSubject<>(null);
    private final BehaviorSubject<VersionFilter> selectVersionFilterAction = new BehaviorSubject<>(null);


    public BehaviorSubject<VersionFilter> getSelectVersionFilterAction() { return selectVersionFilterAction; }


    public VersionFilterController(VersionFilterView versionFilterView) {
        this.versionFilterView = versionFilterView;

        this.versionFilterView.bindVersionFilter(this.versionFilter);
        this.versionFilterView.bindSelectVersionFilterAction(this.selectVersionFilterAction);

        this.initVersionFilter();
    }


    private void initVersionFilter() {
        this.versionFilter.next(
            new VersionFilter(
                VersionInfo.LOW_DATE,
                VersionInfo.HIGH_DATE,
                Pflegestatus.TEST
            )
        );
    }
}
