package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.FwdDependencyItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionAggregateItemConverter;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionPresenter;
import com.tschanz.v_bro.app.usecase.select_version.responsemodel.SelectVersionResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SelectVersionPresenterImpl implements SelectVersionPresenter {
    private final MainModel mainModel;


    @Override
    public void present(@NonNull SelectVersionResponse response) {
        if (!response.isError) {
            this.mainModel.versions.next(new SelectableItemList<>(this.mainModel.versions.getCurrentValue().getItems(), response.selectVersionId));
            this.mainModel.fwdDependencies.next(FwdDependencyItemConverter.fromResponse(response.fwdDependencies));
            this.mainModel.versionAggregate.next(VersionAggregateItemConverter.fromResponse(response.versionAggregate));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
