package com.tschanz.v_bro.app.swing.controller;

import com.tschanz.v_bro.app.swing.model.StatusItem;
import com.tschanz.v_bro.app.swing.view.StatusBarView;
import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.GenericSubscriber;


public class StatusBarController {
    private final StatusBarView statusBarView;
    private final BehaviorSubject<StatusItem> appStatus = new BehaviorSubject<>(null);


    public BehaviorSubject<StatusItem> getAppStatus() { return appStatus; }


    public StatusBarController(StatusBarView statusBarView) {
        this.statusBarView = statusBarView;
        this.appStatus.subscribe(new GenericSubscriber<>(this::onStatusChanged));
    }


    private void onStatusChanged(StatusItem status) {
        if (status == null) {
            this.statusBarView.setStatusInfo("");
        } else if (status.isError()) {
            this.statusBarView.setStatusError(status.getStatusText());
        } else {
            this.statusBarView.setStatusInfo(status.getStatusText());
        }
    }
}
