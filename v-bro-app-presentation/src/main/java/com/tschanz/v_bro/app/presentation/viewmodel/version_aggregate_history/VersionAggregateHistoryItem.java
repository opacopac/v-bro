package com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate_history;

import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class VersionAggregateHistoryItem {
    private final boolean hasPrevious;
    private final boolean hasNext;


    public static VersionAggregateHistoryItem fromResponse(VersionAggregateHistoryResponse response) {
        return new VersionAggregateHistoryItem(response.hasPrevious(), response.hasNext());
    }
}
