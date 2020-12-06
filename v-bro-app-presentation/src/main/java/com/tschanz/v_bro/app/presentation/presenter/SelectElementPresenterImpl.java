package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.FwdDependencyItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionAggregateItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionFilterItemConverter;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.VersionItemConverter;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementPresenter;
import com.tschanz.v_bro.app.usecase.select_element.responsemodel.SelectElementResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SelectElementPresenterImpl implements SelectElementPresenter {
    private final MainModel mainModel;


    @Override
    public void present(@NonNull SelectElementResponse response) {
        if (!response.isError) {
            this.mainModel.elements.next(new SelectableItemList<>(this.mainModel.elements.getCurrentValue().getItems(), response.selectElementId));
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
