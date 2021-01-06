package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.presenter.version_filter.VersionFilterPresenter;
import com.tschanz.v_bro.app.presenter.version_filter.VersionFilterResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionFilterPresenterImpl implements VersionFilterPresenter {
    private final BehaviorSubject<VersionFilterItem> versionFilter;


    @Override
    public void present(VersionFilterResponse response) {
        var item = VersionFilterItem.fromResponse(response);

        this.versionFilter.next(item);
    }
}
