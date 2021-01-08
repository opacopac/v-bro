package com.tschanz.v_bro.app.usecase.select_version_aggregate_history;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SelectVersionAggregateHistoryRequest {
    private final boolean goFwd;
}
