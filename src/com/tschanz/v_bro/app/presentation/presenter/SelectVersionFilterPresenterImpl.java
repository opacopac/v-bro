package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterPresenter;
import com.tschanz.v_bro.app.usecase.select_version_filter.responsemodel.SelectVersionFilterResponse;

import java.util.List;
import java.util.stream.Collectors;


public class SelectVersionFilterPresenterImpl implements SelectVersionFilterPresenter {
    private final MainModel mainModel;


    public SelectVersionFilterPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectVersionFilterResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.versions.next(this.getVersionItems(response.versions));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }


    private List<VersionItem> getVersionItems(List<VersionResponse> versions) {
        return versions
            .stream()
            .map(version -> new VersionItem(version.id, version.gueltigVon, version.gueltigBis))
            .collect(Collectors.toList());
    }
}
