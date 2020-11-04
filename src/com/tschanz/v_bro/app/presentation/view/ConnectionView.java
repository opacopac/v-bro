package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.QuickConnectionItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface ConnectionView {
    void bindQuickConnectionList(Flow.Publisher<List<QuickConnectionItem>> quickConnectionList);

    void bindConnectToRepoAction(BehaviorSubject<RepoConnectionItem> selectedRepoConnection);

    void bindCurrentRepoConnection(BehaviorSubject<RepoConnectionItem> currentRepoConnection);
}
