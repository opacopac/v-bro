package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.status.StatusItem;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class StatusPresenterImpl implements StatusPresenter {
    private final BehaviorSubject<StatusItem> appStatus;


    @Override
    public void present(StatusResponse response) {
        var statusItem = StatusItem.fromResponse(response);

        this.appStatus.next(statusItem);
    }
}
