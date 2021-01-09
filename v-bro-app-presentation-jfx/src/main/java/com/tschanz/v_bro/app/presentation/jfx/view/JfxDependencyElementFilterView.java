package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyElementFilterController;
import com.tschanz.v_bro.app.presentation.view.DependencyElementFilterView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ResourceBundle;


@Log
public class JfxDependencyElementFilterView implements Initializable, DependencyElementFilterView {
    @FXML public TextField elementQueryTextField;
    private DependencyElementFilterController dependencyElementFilterController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public void bindViewModel(
        DependencyElementFilterController dependencyElementFilterController
    ) {
        this.dependencyElementFilterController = dependencyElementFilterController;
    }


    public void onQueryChanged(ActionEvent actionEvent) {
        var query = this.elementQueryTextField.getText();

        new Thread(() -> { this.dependencyElementFilterController.onDependencyElementQuerySelected(query); }).start();
    }
}
