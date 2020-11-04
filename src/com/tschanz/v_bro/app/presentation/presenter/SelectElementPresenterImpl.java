package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementPresenter;
import com.tschanz.v_bro.app.usecase.select_element.responsemodel.SelectElementResponse;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;

import java.util.List;
import java.util.stream.Collectors;


public class SelectElementPresenterImpl implements SelectElementPresenter {
    private final MainModel mainModel;


    public SelectElementPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectElementResponse response) {
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