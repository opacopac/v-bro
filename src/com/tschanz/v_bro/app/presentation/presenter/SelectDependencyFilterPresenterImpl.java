package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterPresenter;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.SelectDependencyFilterResponse;

import java.util.List;
import java.util.stream.Collectors;


public class SelectDependencyFilterPresenterImpl implements SelectDependencyFilterPresenter {
    private final MainModel mainModel;


    public SelectDependencyFilterPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectDependencyFilterResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.fwdDependencies.next(this.getFwdDependencyItems(response.fwdDependencies));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }


    private List<FwdDependencyItem> getFwdDependencyItems(List<FwdDependencyResponse> fwdDependencies) {
        return fwdDependencies
            .stream()
            .map(dependency -> new FwdDependencyItem(
                dependency.elementClass,
                dependency.elementId,
                this.getVersionItems(dependency.versions)
            ))
            .collect(Collectors.toList());
    }


    private List<VersionItem> getVersionItems(List<VersionResponse> versions) {
        return versions
            .stream()
            .map(version -> new VersionItem(version.id, version.gueltigVon, version.gueltigBis))
            .collect(Collectors.toList());
    }
}
