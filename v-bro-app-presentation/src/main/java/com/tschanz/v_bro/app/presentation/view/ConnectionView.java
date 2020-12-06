package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.QuickConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;

import java.util.List;
import java.util.concurrent.Flow;


public interface ConnectionView {
    void bindViewModel(
        Flow.Publisher<List<QuickConnectionItem>> quickConnectionList,
        ViewAction<RepoConnectionItem> connectToRepoAction,
        BehaviorSubject<RepoConnectionItem> currentRepoConnection
    );
}
