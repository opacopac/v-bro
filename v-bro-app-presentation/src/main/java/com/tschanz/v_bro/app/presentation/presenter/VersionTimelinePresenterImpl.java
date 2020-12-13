package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionTimelinePresenterImpl implements VersionTimelinePresenter {
    public final BehaviorSubject<SelectableItemList<VersionItem>> versions;


    @Override
    public void present(@NonNull VersionTimelineResponse response) {
        var versions = VersionItem.fromResponse(response);

        this.versions.next(versions);
    }
}
