package com.tschanz.v_bro.repo.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.repo.swing.model.QuickConnectionItem;
import com.tschanz.v_bro.repo.swing.model.RepoConnectionItem;

import java.util.List;


public interface ConnectionView {
    void bindQuickConnectionList(BehaviorSubject<List<QuickConnectionItem>> quickConnectionList);

    void bindConnectToRepoAction(BehaviorSubject<RepoConnectionItem> selectedRepoConnection);

    void bindCurrentRepoConnection(BehaviorSubject<RepoConnectionItem> currentRepoConnection);
}
