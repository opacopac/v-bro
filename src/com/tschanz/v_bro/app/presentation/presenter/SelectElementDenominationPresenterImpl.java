package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.ElementItemConverter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationPresenter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel.SelectElementDenominationResponse;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;


public class SelectElementDenominationPresenterImpl implements SelectElementDenominationPresenter {
    private final MainModel mainModel;


    public SelectElementDenominationPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectElementDenominationResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.elements.next(ElementItemConverter.fromResponse(response.elements, null)); // TODO
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
