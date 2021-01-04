package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.DependenciesPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyItem;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;

import java.util.List;


public class JfxDependenciesPresenter extends DependenciesPresenterImpl {
    public JfxDependenciesPresenter(BehaviorSubject<List<DependencyItem>> fwdDependencies) {
        super(fwdDependencies);
    }


    @Override
    public void present(DependencyListResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
