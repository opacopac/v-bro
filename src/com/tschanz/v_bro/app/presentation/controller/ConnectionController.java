package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ConnectToRepoAction;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.*;
import com.tschanz.v_bro.app.usecase.disconnect_repo.requestmodel.CloseConnectionRequest;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.app.presentation.viewmodel.QuickConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.XmlRepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.JdbcRepoConnectionItem;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionUseCase;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionUseCase;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class ConnectionController {
    private static final String QUICKCONNECTIONS_PROPERTY_PREFIX = "quickconnection."; // TODO: move to app
    private final BehaviorSubject<List<QuickConnectionItem>> quickConnectionList;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final OpenConnectionUseCase openConnectionUc;
    private final CloseConnectionUseCase closeConnectionUc;


    public ConnectionController(
        Properties appProperties,
        BehaviorSubject<List<QuickConnectionItem>> quickConnectionList,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        ConnectToRepoAction connectToRepoAction,
        OpenConnectionUseCase openConnectionUc,
        CloseConnectionUseCase closeConnectionUc
    ) {
        this.quickConnectionList = quickConnectionList;
        this.repoConnection = repoConnection;
        this.openConnectionUc = openConnectionUc;
        this.closeConnectionUc = closeConnectionUc;

        connectToRepoAction.subscribe(new GenericSubscriber<>(this::onConnectToRepoAction));

        this.initQuickConnectionList(appProperties);
    }


    // TODO => move to app
    private void initQuickConnectionList(Properties appProperties) {
        List<QuickConnectionItem> quickConnectionItems = appProperties.stringPropertyNames()
            .stream()
            .filter(key -> key.startsWith(QUICKCONNECTIONS_PROPERTY_PREFIX))
            .sorted()
            .map(key -> this.parseQuickConnectionProperty(appProperties.getProperty(key)))
            .collect(Collectors.toList());

        this.quickConnectionList.next(quickConnectionItems);
    }


    // TODO => move to app
    private QuickConnectionItem parseQuickConnectionProperty(String propertyValue) {
        String[] parts = propertyValue.split(",", -1); // -1: don't cut of trailing empty parts
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid quick connection '" + propertyValue + "': invalid number of comma separators");
        }

        RepoType repoType = RepoType.valueOf(parts[1]);

        return new QuickConnectionItem(parts[0], repoType, parts[2], parts[3], parts[4], parts[5]);
    }


    private void onConnectToRepoAction(RepoConnectionItem connection) {
        if (connection != null) {
            OpenConnectionRequest request = this.getOpenConnectionRequest(connection);
            this.openConnectionUc.execute(request);
        } else if (this.repoConnection.getCurrentValue() != null) {
            CloseConnectionRequest request = new CloseConnectionRequest(this.repoConnection.getCurrentValue().repoType);
            this.closeConnectionUc.execute(request);
        }
    }


    private OpenConnectionRequest getOpenConnectionRequest(RepoConnectionItem connection) {
        ConnectionParametersRequest connectionParameters;
        switch (connection.getRepoType()) {
            case JDBC:
                JdbcRepoConnectionItem jdbcConnection = (JdbcRepoConnectionItem) connection;
                connectionParameters = new JdbcConnectionParametersRequest(jdbcConnection.getUrl(), jdbcConnection.getUser(), jdbcConnection.getPassword());
                break;
            case XML:
                XmlRepoConnectionItem xmlRepoConnectionItem = (XmlRepoConnectionItem) connection;
                connectionParameters = new XMlConnectionParametersRequest(xmlRepoConnectionItem.getFilename());
                break;
            case MOCK:
            default:
                connectionParameters = new MockConnectionParametersRequest();
                break;
        }
        return new OpenConnectionRequest(connectionParameters);
    }
}
