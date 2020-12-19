package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.VersionTimelinePresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxVersionTimelinePresenter extends VersionTimelinePresenterImpl {
    public JfxVersionTimelinePresenter(BehaviorSubject<SelectableItemList<VersionItem>> versions) {
        super(versions);
    }


    @Override
    public void present(VersionTimelineResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
