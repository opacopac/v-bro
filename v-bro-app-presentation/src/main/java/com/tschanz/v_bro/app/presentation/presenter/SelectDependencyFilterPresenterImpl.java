package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.FwdDependencyItemConverter;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterPresenter;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.SelectDependencyFilterResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SelectDependencyFilterPresenterImpl implements SelectDependencyFilterPresenter {
    private final MainModel mainModel;


    @Override
    public void present(@NonNull SelectDependencyFilterResponse response) {
        if (!response.isError) {
            this.mainModel.fwdDependencies.next(FwdDependencyItemConverter.fromResponse(response.fwdDependencies));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
