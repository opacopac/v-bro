package com.tschanz.v_bro.app.presentation.jfx;

import com.tschanz.v_bro.app.presentation.controller.MainController;
import com.tschanz.v_bro.app.presentation.jfx.view.JfxMainView;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class JfxApplication extends Application {
    private static final String MAIN_VIEW_FXML = "MainView.fxml";
    private static final String WINDOW_TITLE = "V-Bro - Version Browser";
    private static final String ICON_FILE = "v-bro.png";
    private static MainViewModel mainViewModel;
    private static MainController mainController;


    public static void main(String[] args, MainViewModel mainViewModel, MainController mainController) {
        JfxApplication.mainViewModel = mainViewModel;
        JfxApplication.mainController = mainController;
        launch(args);
    }


    @Override
    @SneakyThrows
    public void start(Stage stage) {
        var mainViewUrl = getClass().getClassLoader().getResource(MAIN_VIEW_FXML);
        var fxmlLoader = new FXMLLoader(mainViewUrl);
        Parent root = fxmlLoader.load();
        JfxMainView jfxMainView = fxmlLoader.getController();
        jfxMainView.bindViewModel(mainViewModel, mainController);
        var icon = new Image(ICON_FILE);
        stage.getIcons().add(icon);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(root));
        stage.show();
        this.fitToScreen(stage);
    }


    private void fitToScreen(Stage stage) {
        stage.setX(Math.max(0, stage.getX()));
        stage.setY(Math.max(0, stage.getY()));
        stage.setHeight(Math.min(Screen.getPrimary().getBounds().getHeight() * 0.9, stage.getHeight()));
        stage.setWidth(Math.min(Screen.getPrimary().getBounds().getWidth() * 0.9, stage.getWidth()));
    }
}
