package com.tschanz.v_bro.app.presentation.controller;

import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;


public interface ConnectionController {
    void onReadQuickConnections();

    void onConnectToRepo(RepoConnectionItem connection);
}
