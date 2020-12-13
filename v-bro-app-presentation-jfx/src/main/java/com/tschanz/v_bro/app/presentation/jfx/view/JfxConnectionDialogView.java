package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.*;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;


public class JfxConnectionDialogView {
    @FXML private Label urlLabel;
    @FXML private Label userLabel;
    @FXML private Label passwordLabel;
    @FXML private Label fileLabel;
    @FXML private RowConstraints urlRow;
    @FXML private RowConstraints userRow;
    @FXML private RowConstraints passwordRow;
    @FXML private RowConstraints fileRow;
    @FXML private ComboBox<QuickConnectionItem> quickLinkComboBox;
    @FXML private ToggleGroup repoType;
    @FXML private RadioButton jdbcRadioButton;
    @FXML private RadioButton xmlRadioButton;
    @FXML private RadioButton mockRadioButton;
    @FXML private TextField urlTextField;
    @FXML private TextField userTextField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fileTextField;
    @FXML private Button connectButton;
    @FXML private Button cancelButton;
    @Setter private Stage stage;
    private ViewAction<RepoConnectionItem> connectToRepoAction;
    private BehaviorSubject<RepoConnectionItem> currentRepoConnection;


    public void bindViewModel(
        Flow.Publisher<List<QuickConnectionItem>> quickConnectionList,
        ViewAction<RepoConnectionItem> connectToRepoAction,
        BehaviorSubject<RepoConnectionItem> currentRepoConnection
    ) {
        this.connectToRepoAction = connectToRepoAction;
        this.currentRepoConnection = currentRepoConnection;
        quickConnectionList.subscribe(new GenericSubscriber<>(this::onQuickConnectionListChanged));
        this.showHideConnectionFields(RepoType.JDBC);
    }


    @FXML
    private void onQuickLinkSelected(ActionEvent actionEvent) {
        var quickConnectionItem = this.quickLinkComboBox.getValue();
        this.showHideConnectionFields(quickConnectionItem.getRepoType());
        this.urlTextField.setText(quickConnectionItem.getUrl());
        this.userTextField.setText(quickConnectionItem.getUser());
        this.passwordField.setText(quickConnectionItem.getPassword());
        this.fileTextField.setText(quickConnectionItem.getFilename());
    }


    @FXML
    private void onJdbcRadioButtonSelected(ActionEvent actionEvent) {
        this.showHideConnectionFields(RepoType.JDBC);
    }


    @FXML
    private void onXmlRadioButtonSelected(ActionEvent actionEvent) {
        this.showHideConnectionFields(RepoType.XML);
    }


    @FXML
    private void onMockRadioButtonSelected(ActionEvent actionEvent) {
        this.showHideConnectionFields(RepoType.MOCK);
    }


    @FXML
    private void onConnectClicked(ActionEvent actionEvent) {
        RepoType repoType = this.getSelectedRepoType();
        RepoConnectionItem nextRepoConnection;
        switch (repoType) {
            case JDBC:
                nextRepoConnection = new JdbcRepoConnectionItem(this.urlTextField.getText(), this.userTextField.getText(), String.valueOf(this.passwordField.getText()));
                break;
            case XML:
                nextRepoConnection = new XmlRepoConnectionItem(this.fileTextField.getText());
                break;
            case MOCK:
            default:
                nextRepoConnection = new MockRepoConnectionItem();
                break;
        }

        this.connectToRepoAction.next(nextRepoConnection);
        this.stage.close();
    }


    @FXML
    private void onCancelClicked(ActionEvent actionEvent) {
        this.stage.close();
    }


    private void onQuickConnectionListChanged(List<QuickConnectionItem> quickConnectionList) {
        List<QuickConnectionItem> entries = new ArrayList<>(quickConnectionList);
        this.quickLinkComboBox.setItems(FXCollections.observableList(entries));
    }


    private RepoType getSelectedRepoType() {
        if (this.jdbcRadioButton.isSelected()) {
            return RepoType.JDBC;
        } else if (this.xmlRadioButton.isSelected()) {
            return RepoType.XML;
        } else {
            return RepoType.MOCK;
        }
    }


    private void showHideConnectionFields(RepoType repoType) {
        this.jdbcRadioButton.setSelected(repoType == RepoType.JDBC);
        this.xmlRadioButton.setSelected(repoType == RepoType.XML);
        this.mockRadioButton.setSelected(repoType == RepoType.MOCK);

        this.urlLabel.setVisible(repoType == RepoType.JDBC);
        this.userLabel.setVisible(repoType == RepoType.JDBC);
        this.passwordLabel.setVisible(repoType == RepoType.JDBC);
        this.fileLabel.setVisible(repoType == RepoType.XML);

        this.urlTextField.setVisible(repoType == RepoType.JDBC);
        this.userTextField.setVisible(repoType == RepoType.JDBC);
        this.passwordField.setVisible(repoType == RepoType.JDBC);
        this.fileTextField.setVisible(repoType == RepoType.XML);

        var jdbcMaxHeight = repoType == RepoType.JDBC ? Double.MAX_VALUE : 0;
        var xmlMaxHeight = repoType == RepoType.XML ? Double.MAX_VALUE : 0;

        this.urlRow.setMaxHeight(jdbcMaxHeight);
        this.userRow.setMaxHeight(jdbcMaxHeight);
        this.passwordRow.setMaxHeight(jdbcMaxHeight);
        this.fileRow.setMaxHeight(xmlMaxHeight);
    }
}
