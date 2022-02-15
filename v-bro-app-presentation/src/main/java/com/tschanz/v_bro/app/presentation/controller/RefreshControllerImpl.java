package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.usecase.refresh_view.RefreshViewRequest;
import com.tschanz.v_bro.app.usecase.refresh_view.RefreshViewUseCase;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RefreshControllerImpl implements RefreshController {
    private final RefreshViewUseCase refreshViewUc;
    private final ProgressController progressController;


    @Override
    public void refreshView() {
        this.progressController.startProgress();

        var request = new RefreshViewRequest();
        this.refreshViewUc.execute(request);

        this.progressController.endProgress();
    }
}
