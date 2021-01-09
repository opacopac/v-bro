package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.repo.QuickConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoRequest;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_repo.OpenRepoUseCase;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.repo.domain.model.RepoType;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class ConnectionControllerImpl implements ConnectionController {
    private static final String QUICKCONNECTIONS_PROPERTY_PREFIX = "quickconnection."; // TODO: move to app
    private final BehaviorSubject<List<QuickConnectionItem>> quickConnectionList;
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final OpenRepoUseCase openRepoUc;
    private final CloseRepoUseCase closeRepoUc;
    private final ProgressController progressController;


    public ConnectionControllerImpl(
        Properties appProperties,
        BehaviorSubject<List<QuickConnectionItem>> quickConnectionList,
        BehaviorSubject<RepoConnectionItem> repoConnection,
        OpenRepoUseCase openRepoUc,
        CloseRepoUseCase closeRepoUc,
        ProgressController progressController
    ) {
        this.quickConnectionList = quickConnectionList;
        this.repoConnection = repoConnection;
        this.openRepoUc = openRepoUc;
        this.closeRepoUc = closeRepoUc;
        this.progressController = progressController;

        this.initQuickConnectionList(appProperties);
    }


    @Override
    public void onConnectToRepo(RepoConnectionItem connection) {
        this.progressController.startProgress();

        if (connection != null) {
            var request = RepoConnectionItem.toRequest(connection);
            this.openRepoUc.execute(request);
        } else if (this.repoConnection.getCurrentValue() != null) {
            var request = new CloseRepoRequest();
            this.closeRepoUc.execute(request);
        }

        this.progressController.endProgress();
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
}
