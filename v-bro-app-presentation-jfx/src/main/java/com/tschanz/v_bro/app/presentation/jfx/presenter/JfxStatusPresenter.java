package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.StatusPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.status.StatusItem;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxStatusPresenter extends StatusPresenterImpl {
    public JfxStatusPresenter(BehaviorSubject<StatusItem> appStatus) {
        super(appStatus);
    }


    @Override
    public void present(StatusResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
