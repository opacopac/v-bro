package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate_history.VersionAggregateHistoryItem;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryPresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionAggregateHistoryPresenterImpl implements VersionAggregateHistoryPresenter {
    private final BehaviorSubject<VersionAggregateHistoryItem> versionAggregateHistory;


    @Override
    public void present(VersionAggregateHistoryResponse response) {
        var item = VersionAggregateHistoryItem.fromResponse(response);

        this.versionAggregateHistory.next(item);
    }
}
