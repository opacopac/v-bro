package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.FwdDependencyItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionAggregateItemConverter;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionPresenter;
import com.tschanz.v_bro.app.usecase.select_version.responsemodel.SelectVersionResponse;


public class SelectVersionPresenterImpl implements SelectVersionPresenter {
    private final MainModel mainModel;


    public SelectVersionPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectVersionResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.versions.next(new SelectedItemList<>(this.mainModel.versions.getCurrentValue().getItems(), response.selectVersionId));
            this.mainModel.fwdDependencies.next(FwdDependencyItemConverter.fromResponse(response.fwdDependencies));
            this.mainModel.versionAggregate.next(VersionAggregateItemConverter.fromResponse(response.versionAggregate));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
