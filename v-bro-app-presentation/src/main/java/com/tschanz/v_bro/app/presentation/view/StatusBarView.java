package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.status.StatusItem;

import java.util.concurrent.Flow;


public interface StatusBarView {
    void bindViewModel(Flow.Publisher<StatusItem> status);
}
