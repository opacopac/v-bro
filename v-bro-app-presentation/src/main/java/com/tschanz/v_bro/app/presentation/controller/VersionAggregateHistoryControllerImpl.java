package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.select_version_aggregate_history.SelectVersionAggregateHistoryRequest;
import com.tschanz.v_bro.app.usecase.select_version_aggregate_history.SelectVersionAggregateHistoryUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionAggregateHistoryControllerImpl implements VersionAggregateHistoryController {
    private final SelectVersionAggregateHistoryUseCase selectVersionAggregateHistoryUc;


    @Override
    public void onForwardSelected() {
        var request = new SelectVersionAggregateHistoryRequest(true);
        this.selectVersionAggregateHistoryUc.execute(request);
    }


    @Override
    public void onBackwardSelected() {
        var request = new SelectVersionAggregateHistoryRequest(false);
        this.selectVersionAggregateHistoryUc.execute(request);
    }
}
