package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoRequest;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoUseCase;
import com.tschanz.v_bro.app.usecase.open_repo.OpenRepoUseCase;
import com.tschanz.v_bro.app.usecase.read_quick_connections.ReadQuickConnectionsRequest;
import com.tschanz.v_bro.app.usecase.read_quick_connections.ReadQuickConnectionsUseCase;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ConnectionControllerImpl implements ConnectionController {
    private final BehaviorSubject<RepoConnectionItem> repoConnection;
    private final ReadQuickConnectionsUseCase readQuickConnectionsUc;
    private final OpenRepoUseCase openRepoUc;
    private final CloseRepoUseCase closeRepoUc;
    private final ProgressController progressController;


    @Override
    public void onReadQuickConnections() {
        var request = new ReadQuickConnectionsRequest();
        this.readQuickConnectionsUc.execute(request);
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
}
