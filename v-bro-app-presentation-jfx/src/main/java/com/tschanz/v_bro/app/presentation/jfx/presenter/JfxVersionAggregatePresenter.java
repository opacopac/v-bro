package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.VersionAggregatePresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate.VersionAggregateItem;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregateResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxVersionAggregatePresenter extends VersionAggregatePresenterImpl {
    public JfxVersionAggregatePresenter(BehaviorSubject<VersionAggregateItem> versionAggregate) {
        super(versionAggregate);
    }


    @Override
    public void present(VersionAggregateResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
