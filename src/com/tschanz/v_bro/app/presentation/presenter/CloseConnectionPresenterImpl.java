package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionPresenter;
import com.tschanz.v_bro.app.usecase.disconnect_repo.responsemodel.CloseConnectionResponse;

import java.util.Collections;


public class CloseConnectionPresenterImpl implements CloseConnectionPresenter {
    private final MainModel mainModel;


    public CloseConnectionPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }



    @Override
    public void present(CloseConnectionResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.currentRepoConnection.next(null);
            this.mainModel.elementClasses.next(Collections.emptyList());
            this.mainModel.elementDenominations.next(Collections.emptyList());
            this.mainModel.elements.next(Collections.emptyList());
            this.mainModel.versions.next(Collections.emptyList());
            this.mainModel.fwdDependencies.next(Collections.emptyList());
            this.mainModel.selectElementClassAction.next(null);
            this.mainModel.selectVersionFilterAction.next(null);
            this.mainModel.selectElementAction.next(null);
            this.mainModel.selectVersionAction.next(null);
            this.mainModel.versionAggregate.next(null);
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
