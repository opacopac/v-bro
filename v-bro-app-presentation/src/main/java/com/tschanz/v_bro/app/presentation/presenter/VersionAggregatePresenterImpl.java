package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate.VersionAggregateItem;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregatePresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregateResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionAggregatePresenterImpl implements VersionAggregatePresenter {
    public final BehaviorSubject<VersionAggregateItem> versionAggregate;


    @Override
    public void present(VersionAggregateResponse response) {
        var versionAggregateItem = VersionAggregateItem.fromResponse(response);

        this.versionAggregate.next(versionAggregateItem);
    }
}
