package com.tschanz.v_bro.app.presentation.jfx;

import com.tschanz.v_bro.app.presentation.jfx.view.JfxMainView;
import com.tschanz.v_bro.app.presentation.actions.MainActions;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class JfxApplication extends Application {
    private static final String MAIN_VIEW_FXML = "MainView.fxml";
    private static final String WINDOW_TITLE = "V-Bro - Version Browser";
    private static MainModel mainModel;
    private static MainActions mainActions;


    public static void main(String[] args, MainModel mainModel, MainActions mainActions) {
        JfxApplication.mainModel = mainModel;
        JfxApplication.mainActions = mainActions;
        launch(args);
    }


    @Override
    @SneakyThrows
    public void start(Stage stage) {
        var mainViewUrl = getClass().getClassLoader().getResource(MAIN_VIEW_FXML);
        var fxmlLoader = new FXMLLoader(mainViewUrl);
        Parent root = fxmlLoader.load();
        JfxMainView jfxMainView = fxmlLoader.getController();
        jfxMainView.bindViewModel(mainModel, mainActions);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
