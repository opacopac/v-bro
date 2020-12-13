package com.tschanz.v_bro.app.presentation.jfx.view;


import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.view.ConnectionView;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.JdbcRepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.QuickConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.XmlRepoConnectionItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;


public class JfxConnectionView implements Initializable, ConnectionView {
    private static final String DIALOG_VIEW_FXML = "ConnectionDialogView.fxml";
    private static final int MAX_NAME_LENGTH = 75;
    private static final String NOT_CONNECTED_VALUE = "(not connected)";
    private static final String MOCK_CONNECTION_VALUE = "(Mock connection)";
    private Stage connectionDialog;
    private JfxConnectionDialogView connectionDialogView;
    @FXML private Button connectButton;
    @FXML private Button disconnectButton;
    @FXML private Label connectionLabel;
    private ViewAction<RepoConnectionItem> connectToRepoAction;


    @Override
    @SneakyThrows
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.connectionDialog = new Stage(StageStyle.DECORATED);
        this.connectionDialog.initModality(Modality.APPLICATION_MODAL);
        var dialogViewUrl = getClass().getClassLoader().getResource(DIALOG_VIEW_FXML);
        var fxmlLoader = new FXMLLoader(dialogViewUrl);
        Parent root = fxmlLoader.load();
        var scene = new Scene(root);
        this.connectionDialog.setScene(scene);
        this.connectionDialogView = fxmlLoader.getController();
        this.connectionDialogView.setStage(this.connectionDialog);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<List<QuickConnectionItem>> quickConnectionList,
        ViewAction<RepoConnectionItem> connectToRepoAction,
        BehaviorSubject<RepoConnectionItem> currentRepoConnection
    ) {
        this.connectToRepoAction = connectToRepoAction;
        this.connectionDialogView.bindViewModel(quickConnectionList, connectToRepoAction, currentRepoConnection);
        currentRepoConnection.subscribe(new GenericSubscriber<>(this::onRepoConnectionChanged));
    }


    @FXML
    private void onConnectClicked(javafx.event.ActionEvent actionEvent) {
        if (this.connectionDialog.getOwner() == null) {
            this.connectionDialog.initOwner(this.connectButton.getScene().getWindow());
        }
        this.connectionDialog.show();
    }


    @FXML
    private void onDisconnectClicked(ActionEvent actionEvent) {
        this.connectToRepoAction.next(null);
    }


    private void onRepoConnectionChanged(RepoConnectionItem repoConnection) {
        String conStr = this.createConnectionString(repoConnection);
        this.connectionLabel.setText(conStr);
        this.connectButton.setVisible(repoConnection == null);
        this.disconnectButton.setVisible(repoConnection != null);
    }


    // TODO => presenter
    private String createConnectionString(RepoConnectionItem repoConnection) {
        String conStr;
        if (repoConnection == null) {
            conStr = NOT_CONNECTED_VALUE;
        } else if (repoConnection.repoType == RepoType.JDBC) {
            JdbcRepoConnectionItem jdbcRepoConnection = (JdbcRepoConnectionItem) repoConnection;
            String user = jdbcRepoConnection.getUser();
            conStr = jdbcRepoConnection.getUrl() + ((user == null || user.isEmpty()) ? "" : " (" + user + ")");
        } else if (repoConnection.repoType == RepoType.XML) {
            conStr = ((XmlRepoConnectionItem) repoConnection).getFilename();
        } else {
            conStr = MOCK_CONNECTION_VALUE;
        }

        if (conStr.length() <= MAX_NAME_LENGTH) {
            return conStr;
        } else {
            return conStr.substring(0, MAX_NAME_LENGTH) + "...";
        }
    }
}
