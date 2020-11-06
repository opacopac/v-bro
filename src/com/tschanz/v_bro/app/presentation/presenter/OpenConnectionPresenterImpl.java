package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.ConnectionItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.ElementClassItemConverter;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionPresenter;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.OpenConnectionResponse;


public class OpenConnectionPresenterImpl implements OpenConnectionPresenter {
    private final MainModel mainModel;


    public OpenConnectionPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(OpenConnectionResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.currentRepoConnection.next(ConnectionItemConverter.fromResponse(response.repoConnection));
            this.mainModel.elementClasses.next(ElementClassItemConverter.fromResponse(response.elementClasses, null)); // TODO: provide in response
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
