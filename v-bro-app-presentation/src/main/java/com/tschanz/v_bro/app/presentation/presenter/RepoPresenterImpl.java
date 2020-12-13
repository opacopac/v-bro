package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.presenter.repo.RepoPresenter;
import com.tschanz.v_bro.app.presenter.repo.RepoResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RepoPresenterImpl implements RepoPresenter {
    private final BehaviorSubject<RepoConnectionItem> currentRepoConnection;


    @Override
    public void present(RepoResponse response) {
        var statusItem = RepoConnectionItem.fromResponse(response);

        this.currentRepoConnection.next(statusItem);
    }
}
