package com.tschanz.v_bro.repo.presentation.swing.controller;

import com.tschanz.v_bro.app.presentation.swing.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.swing.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.swing.viewmodel.StatusItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.persistence.mock.model.MockConnectionParameters;
import com.tschanz.v_bro.repo.presentation.swing.viewmodel.QuickConnectionItem;
import com.tschanz.v_bro.repo.presentation.swing.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.repo.presentation.swing.viewmodel.XmlRepoConnectionItem;
import com.tschanz.v_bro.repo.presentation.swing.view.ConnectionView;
import com.tschanz.v_bro.repo.presentation.swing.viewmodel.JdbcRepoConnectionItem;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;
import com.tschanz.v_bro.repo.persistence.xml.model.XmlConnectionParameters;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class ConnectionController {
    private static final String QUICKCONNECTIONS_PROPERTY_PREFIX = "quickconnection.";
    private final ConnectionView connectionView;
    private final OpenConnectionUseCase openConnectionUc;
    private final CloseConnectionUseCase closeConnectionUc;
    private final BehaviorSubject<StatusItem> appStatus;
    private final BehaviorSubject<List<QuickConnectionItem>> quickConnectionList = new BehaviorSubject<>(Collections.emptyList());
    private final BehaviorSubject<RepoConnectionItem> currentRepoConnection = new BehaviorSubject<>(null);
    private final BehaviorSubject<RepoConnectionItem> connectToRepoAction = new BehaviorSubject<>(null);


    public BehaviorSubject<RepoConnectionItem> getCurrentRepoConnection() { return this.currentRepoConnection; }



    public ConnectionController(
        Properties appProperties,
        ConnectionView connectionView,
        BehaviorSubject<StatusItem> appStatus,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc
    ) {
        this.connectionView = connectionView;
        this.appStatus = appStatus;
        this.openConnectionUc = openConnectionUc;
        this.closeConnectionUc = closeConnectionUc;

        this.connectionView.bindQuickConnectionList(this.quickConnectionList);
        this.connectionView.bindConnectToRepoAction(this.connectToRepoAction);
        this.connectionView.bindCurrentRepoConnection(this.currentRepoConnection);

        this.connectToRepoAction.subscribe(new GenericSubscriber<>(this::onConnectToRepoAction));

        this.initQuickConnectionList(appProperties);
    }


    private void initQuickConnectionList(Properties appProperties) {
        List<QuickConnectionItem> quickConnectionItems = appProperties.stringPropertyNames()
            .stream()
            .filter(key -> key.startsWith(QUICKCONNECTIONS_PROPERTY_PREFIX))
            .sorted()
            .map(key -> this.parseQuickConnectionProperty(appProperties.getProperty(key)))
            .collect(Collectors.toList());

        this.quickConnectionList.next(quickConnectionItems);
    }


    private QuickConnectionItem parseQuickConnectionProperty(String propertyValue) {
        String[] parts = propertyValue.split(",", -1); // -1: don't cut of trailing empty parts
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid quick connection '" + propertyValue + "': invalid number of comma separators");
        }

        RepoType repoType = RepoType.valueOf(parts[1]);

        return new QuickConnectionItem(parts[0], repoType, parts[2], parts[3], parts[4], parts[5]);
    }


    private void onConnectToRepoAction(RepoConnectionItem connection) {
        try {
            if (connection == null) {
                this.closeConnection();
            } else {
                this.openConnection(connection);
            }
        } catch (VBroAppException exception) {
            this.appStatus.next(new ErrorStatusItem(exception.getMessage()));
        }
    }


    private void openConnection(RepoConnectionItem connection) throws VBroAppException {
        ConnectionParameters connectionParameters;
        switch (connection.getRepoType()) {
            case JDBC:
                JdbcRepoConnectionItem jdbcConnection = (JdbcRepoConnectionItem) connection;
                connectionParameters = new JdbcConnectionParameters(jdbcConnection.getUrl(), jdbcConnection.getUser(), jdbcConnection.getPassword());
                break;
            case XML:
                XmlRepoConnectionItem xmlRepoConnectionItem = (XmlRepoConnectionItem) connection;
                connectionParameters = new XmlConnectionParameters(xmlRepoConnectionItem.getFilename());
                break;
            case MOCK:
            default:
                connectionParameters = new MockConnectionParameters();
                break;
        }

        this.openConnectionUc.connect(connectionParameters);

        this.currentRepoConnection.next(connection);
        this.appStatus.next(new InfoStatusItem("repo connection established"));
    }


    private void closeConnection() throws VBroAppException {
        this.closeConnectionUc.disconnect();

        this.currentRepoConnection.next(null);
        this.appStatus.next(new InfoStatusItem("repo connection closed"));
    }
}
