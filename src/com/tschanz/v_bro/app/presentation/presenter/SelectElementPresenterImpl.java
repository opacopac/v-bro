package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionItemConverter;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementPresenter;
import com.tschanz.v_bro.app.usecase.select_element.responsemodel.SelectElementResponse;


public class SelectElementPresenterImpl implements SelectElementPresenter {
    private final MainModel mainModel;


    public SelectElementPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectElementResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.effectiveVersionFilter.next(VersionFilterItemConverter.fromResponse(response.effectiveVersionFilter));
            this.mainModel.versions.next(VersionItemConverter.fromResponse(response.versions));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
