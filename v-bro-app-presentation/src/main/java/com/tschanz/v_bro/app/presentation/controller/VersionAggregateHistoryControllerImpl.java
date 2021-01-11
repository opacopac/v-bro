package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.select_version_aggregate_history.SelectVersionAggregateHistoryRequest;
import com.tschanz.v_bro.app.usecase.select_version_aggregate_history.SelectVersionAggregateHistoryUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionAggregateHistoryControllerImpl implements VersionAggregateHistoryController {
    private final SelectVersionAggregateHistoryUseCase selectVersionAggregateHistoryUc;
    private final ProgressController progressController;


    @Override
    public void goForward() {
        this.progressController.startProgress();

        var request = new SelectVersionAggregateHistoryRequest(true);
        this.selectVersionAggregateHistoryUc.execute(request);

        this.progressController.endProgress();
    }


    @Override
    public void goBack() {
        this.progressController.startProgress();

        var request = new SelectVersionAggregateHistoryRequest(false);
        this.selectVersionAggregateHistoryUc.execute(request);

        this.progressController.endProgress();
    }
}
