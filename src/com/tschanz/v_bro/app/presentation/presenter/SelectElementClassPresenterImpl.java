package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.DenominationItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.ElementItemConverter;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassPresenter;
import com.tschanz.v_bro.app.usecase.select_element_class.responsemodel.SelectElementClassResponse;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;


public class SelectElementClassPresenterImpl implements SelectElementClassPresenter {
    private final MainModel mainModel;


    public SelectElementClassPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectElementClassResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.elementDenominations.next(DenominationItemConverter.fromResponse(response.denominations));
            this.mainModel.elements.next(ElementItemConverter.fromResponse(response.elements));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}

