package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.RepoPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.presenter.repo.RepoResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxRepoPresenter extends RepoPresenterImpl {
    public JfxRepoPresenter(BehaviorSubject<RepoConnectionItem> currentRepoConnection) {
        super(currentRepoConnection);
    }


    @Override
    public void present(RepoResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
