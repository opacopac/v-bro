package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.*;
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
            this.mainModel.elementClasses.next(ElementClassItemConverter.fromResponse(response.elementClasses, response.selectElementClass));
            this.mainModel.elementDenominations.next(DenominationItemConverter.fromResponse(response.denominations, response.selectDenominations));
            this.mainModel.elements.next(ElementItemConverter.fromResponse(response.elements, response.selectElementId));
            this.mainModel.versionFilter.next(VersionFilterItemConverter.fromResponse(response.versionFilter));
            this.mainModel.effectiveVersionFilter.next(VersionFilterItemConverter.fromResponse(response.effectiveVersionFilter));
            this.mainModel.versions.next(VersionItemConverter.fromResponse(response.versions, response.selectVersionId));
            this.mainModel.fwdDependencies.next(FwdDependencyItemConverter.fromResponse(response.fwdDependencies));
            this.mainModel.versionAggregate.next(VersionAggregateItemConverter.fromResponse(response.versionAggregate));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
