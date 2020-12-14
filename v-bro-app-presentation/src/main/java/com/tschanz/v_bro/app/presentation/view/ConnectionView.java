package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.ConnectionController;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.QuickConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;

import java.util.List;
import java.util.concurrent.Flow;


public interface ConnectionView {
    void bindViewModel(
        Flow.Publisher<List<QuickConnectionItem>> quickConnectionList,
        BehaviorSubject<RepoConnectionItem> currentRepoConnection,
        ConnectionController connectionController
    );
}
