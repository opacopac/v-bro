package com.tschanz.v_bro.repo.swing.controller;

import com.tschanz.v_bro.app.swing.model.ErrorStatusItem;
import com.tschanz.v_bro.app.swing.model.InfoStatusItem;
import com.tschanz.v_bro.app.swing.model.StatusItem;
import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.mock.model.MockConnectionParameters;
import com.tschanz.v_bro.repo.swing.model.RepoConnectionItem;
import com.tschanz.v_bro.repo.swing.view.ConnectionView;
import com.tschanz.v_bro.repo.swing.model.QuickConnectionItem;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;
import com.tschanz.v_bro.repo.xml.model.XmlConnectionParameters;


import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;


public class ConnectionController {
    private final ConnectionView connectionView;
    private final OpenConnectionUseCase openConnectionUc;
    private final CloseConnectionUseCase closeConnectionUc;
    private final BehaviorSubject<StatusItem> appStatus;
    private BehaviorSubject<RepoConnectionItem> currentConnection = new BehaviorSubject<>(null);


    public BehaviorSubject<RepoConnectionItem> getCurrentConnection() { return this.currentConnection; }



    public ConnectionController(
        ConnectionView connectionView,
        BehaviorSubject<StatusItem> appStatus,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc
    ) {
        this.connectionView = connectionView;
        this.appStatus = appStatus;
        this.openConnectionUc = openConnectionUc;
        this.closeConnectionUc = closeConnectionUc;

        this.connectionView.setConnectedState(false);
        this.connectionView.addShowConnectionDialogListener(this::onOpenDialogClicked);
        this.connectionView.addQuickConnectionItemListener(this::onQuickConnectionSelected);
        this.connectionView.addConnectListener(this::onConnectClicked);
        this.connectionView.addDisconnectListener(this::onDisconnectClicked);
    }


    private void onOpenDialogClicked(ActionEvent e) {
        this.connectionView.showConnectionDialog(true);
    }


    private void onQuickConnectionSelected(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            QuickConnectionItem quickConnectionItem = (QuickConnectionItem) itemEvent.getItem();
            if (quickConnectionItem.getRepoType() == RepoType.JDBC) {
                this.connectionView.setJdbcConnectionData(
                    quickConnectionItem.getUrl(),
                    quickConnectionItem.getUser(),
                    quickConnectionItem.getPassword()
                );
            } else if (quickConnectionItem.getRepoType() == RepoType.XML) {
                this.connectionView.setXmlConnectionData(quickConnectionItem.getFilename());
            } else if (quickConnectionItem.getRepoType() == RepoType.MOCK) {
                this.connectionView.setMockConnectionData();
            }
        }
    }


    private void onConnectClicked(ActionEvent e) {
        try {
            this.connectionView.showConnectionDialog(false);

            this.openConnection();

            this.connectionView.setConnectedState(true);
            this.appStatus.next(new InfoStatusItem("repo_connection established"));
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void openConnection() throws VBroAppException {
        ConnectionParameters connectionParameters;
        switch (this.connectionView.getRepoType()) {
            case JDBC:
                connectionParameters = new JdbcConnectionParameters(this.connectionView.getUrl(), this.connectionView.getUser(), this.connectionView.getPassword());
                break;
            case XML:
                connectionParameters = new XmlConnectionParameters(this.connectionView.getFilename());
                break;
            case MOCK:
            default:
                connectionParameters = new MockConnectionParameters(); // TODO: temporary
                break;
        }

        OpenConnectionResponse response = this.openConnectionUc.connect(connectionParameters);
        this.currentConnection.next(new RepoConnectionItem(response.repoConnection.repoType));
    }


    private void onDisconnectClicked(ActionEvent e) {
        try {
            this.connectionView.showConnectionDialog(false);
            this.closeConnectionUc.disconnect();
            this.connectionView.setConnectedState(false);
            this.currentConnection = null;
            this.appStatus.next(new InfoStatusItem("repo_connection closed"));
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }
}
