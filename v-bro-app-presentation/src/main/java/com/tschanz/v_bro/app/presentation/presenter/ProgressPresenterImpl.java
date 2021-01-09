package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presenter.progress.ProgressPresenter;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ProgressPresenterImpl implements ProgressPresenter {
    private final BehaviorSubject<Boolean> progressStatus;


    @Override
    public void present(boolean inProgress) {
        this.progressStatus.next(inProgress);
    }
}
