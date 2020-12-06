package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.*;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionPresenter;
import com.tschanz.v_bro.app.usecase.select_dependency_version.responsemodel.SelectDependencyVersionResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SelectDependencyVersionPresenterImpl implements SelectDependencyVersionPresenter {
    private final MainModel mainModel;


    @Override
    public void present(@NonNull SelectDependencyVersionResponse response) {
        if (!response.isError) {
            SelectableItemList<ElementClassItem> newElementClasses = new SelectableItemList<>(this.mainModel.elementClasses.getCurrentValue().getItems(), response.selectElementClass);
            this.mainModel.elementClasses.next(newElementClasses);
            this.mainModel.elementDenominations.next(DenominationItemConverter.fromResponse(response.denominations, response.selectDenominations));
            this.mainModel.elements.next(ElementItemConverter.fromResponse(response.elements, response.selectElementId));
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
