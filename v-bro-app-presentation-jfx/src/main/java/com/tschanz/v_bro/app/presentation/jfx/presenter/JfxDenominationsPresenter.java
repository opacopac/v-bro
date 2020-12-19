package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.DenominationsPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxDenominationsPresenter extends DenominationsPresenterImpl {
    public JfxDenominationsPresenter(BehaviorSubject<MultiSelectableItemList<DenominationItem>> elementDenominations) {
        super(elementDenominations);
    }


    @Override
    public void present(DenominationListResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
