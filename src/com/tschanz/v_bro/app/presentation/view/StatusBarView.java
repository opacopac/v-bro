package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.StatusItem;

import java.util.concurrent.Flow;


public interface StatusBarView {
    void setStatusInfo(String infoText);

    void setStatusError(String errorText);

    void bindStatus(Flow.Publisher<StatusItem> status);
}
