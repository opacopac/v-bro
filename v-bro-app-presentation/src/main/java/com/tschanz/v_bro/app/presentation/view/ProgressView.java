package com.tschanz.v_bro.app.presentation.view;

import java.util.concurrent.Flow;


public interface ProgressView {
    void bindViewModel(Flow.Publisher<Boolean> progressStatus);
}
