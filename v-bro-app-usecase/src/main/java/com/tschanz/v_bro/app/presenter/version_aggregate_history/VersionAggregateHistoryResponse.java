package com.tschanz.v_bro.app.presenter.version_aggregate_history;

import com.tschanz.v_bro.data_structure.domain.model.VersionFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class VersionAggregateHistoryResponse {
    private final boolean hasPrevious;
    private final boolean hasNext;


    public static VersionAggregateHistoryResponse fromDomain(VersionFilter versionFilter) {
        return null;
    }
}
