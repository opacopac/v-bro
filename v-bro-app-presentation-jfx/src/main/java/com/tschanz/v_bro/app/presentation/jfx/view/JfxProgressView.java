package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.view.ProgressView;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;


public class JfxProgressView implements ProgressView, Initializable {
    @FXML private AnchorPane progressBarPane;
    @FXML private ProgressBar progressBar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.progressBarPane.managedProperty().bind(progressBarPane.visibleProperty());
    }


    public void bindViewModel(Flow.Publisher<Boolean> progressStatus) {
        progressStatus.subscribe(new GenericSubscriber<>(this::onProgressChanged));
    }


    private void onProgressChanged(Boolean inProgress) {
        var isInProgress = inProgress != null ? inProgress : false;

        var width = isInProgress ? 100.0 : 0;
        this.progressBarPane.setVisible(isInProgress);
        this.progressBarPane.setPrefWidth(width);

        var scene = this.progressBarPane.getScene();
        if (scene != null) {
            var cursor = isInProgress ? Cursor.WAIT : Cursor.DEFAULT;
            scene.setCursor(cursor);
        }
    }
}
