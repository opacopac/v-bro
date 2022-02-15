package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.RefreshController;
import com.tschanz.v_bro.app.presentation.view.RefreshView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.java.Log;


@Log
public class JfxRefreshView implements RefreshView {
    @FXML private Button refreshButton;
    private RefreshController refreshController;


    @Override
    public void bindViewModel(RefreshController refreshController) {
        this.refreshController = refreshController;
    }


    @FXML
    public void onRefreshClicked(ActionEvent actionEvent) {
        new Thread(() -> this.refreshController.refreshView()).start();
    }
}
