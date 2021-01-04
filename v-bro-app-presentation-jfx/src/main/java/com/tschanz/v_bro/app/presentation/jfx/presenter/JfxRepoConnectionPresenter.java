package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.RepoConnectionPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.presenter.repo_connection.RepoResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxRepoConnectionPresenter extends RepoConnectionPresenterImpl {
    public JfxRepoConnectionPresenter(BehaviorSubject<RepoConnectionItem> currentRepoConnection) {
        super(currentRepoConnection);
    }


    @Override
    public void present(RepoResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
