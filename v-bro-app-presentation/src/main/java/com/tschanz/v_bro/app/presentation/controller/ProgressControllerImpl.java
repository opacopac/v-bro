package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ProgressControllerImpl implements ProgressController {
    private final BehaviorSubject<Boolean> progressStatus;


    public void startProgress() {
        this.progressStatus.next(true);
    }


    public void endProgress() {
        this.progressStatus.next(false);
    }
}
