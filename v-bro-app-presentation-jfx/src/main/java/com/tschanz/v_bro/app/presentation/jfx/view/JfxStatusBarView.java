package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.view.StatusBarView;
import com.tschanz.v_bro.app.presentation.viewmodel.StatusItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.concurrent.Flow;


public class JfxStatusBarView implements StatusBarView {
    @FXML private Label infoLabel;
    @FXML private Label errorLabel;


    @Override
    public void bindViewModel(Flow.Publisher<StatusItem> status) {
        status.subscribe(new GenericSubscriber<>(this::onStatusChanged));
    }


    private void onStatusChanged(StatusItem status) {
        boolean isErr = status != null && status.isError();
        String text = status != null ? status.getStatusText() : "";

        this.infoLabel.setVisible(!isErr);
        this.errorLabel.setVisible(isErr);

        this.infoLabel.setText(isErr ? "" : text);
        this.errorLabel.setText(isErr ? text : "");
    }
}
