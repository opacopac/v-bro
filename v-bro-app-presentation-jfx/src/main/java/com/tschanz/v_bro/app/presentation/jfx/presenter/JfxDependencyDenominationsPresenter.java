package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.DependencyDenominationsPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxDependencyDenominationsPresenter extends DependencyDenominationsPresenterImpl {
    public JfxDependencyDenominationsPresenter(BehaviorSubject<MultiSelectableItemList<DenominationItem>> dependencyDenominations) {
        super(dependencyDenominations);
    }


    @Override
    public void present(DenominationListResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
