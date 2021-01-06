package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.VersionFilterPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.presenter.version_filter.VersionFilterResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxVersionFilterPresenter extends VersionFilterPresenterImpl {
    public JfxVersionFilterPresenter(BehaviorSubject<VersionFilterItem> versionFilter) {
        super(versionFilter);
    }


    @Override
    public void present(VersionFilterResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
