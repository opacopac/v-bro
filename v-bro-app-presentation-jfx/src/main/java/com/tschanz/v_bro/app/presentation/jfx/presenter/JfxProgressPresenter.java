package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.ProgressPresenterImpl;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxProgressPresenter extends ProgressPresenterImpl {
    public JfxProgressPresenter(BehaviorSubject<Boolean> progressStatus) {
        super(progressStatus);
    }


    @Override
    public void present(boolean inProgress) {
        Platform.runLater(() -> super.present(inProgress));
    }
}
