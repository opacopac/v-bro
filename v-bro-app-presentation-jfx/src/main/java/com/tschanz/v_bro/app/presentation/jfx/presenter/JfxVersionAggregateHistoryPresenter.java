package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.VersionAggregateHistoryPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate_history.VersionAggregateHistoryItem;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxVersionAggregateHistoryPresenter extends VersionAggregateHistoryPresenterImpl {
    public JfxVersionAggregateHistoryPresenter(BehaviorSubject<VersionAggregateHistoryItem> versionAggregateHistory) {
        super(versionAggregateHistory);
    }


    @Override
    public void present(VersionAggregateHistoryResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
