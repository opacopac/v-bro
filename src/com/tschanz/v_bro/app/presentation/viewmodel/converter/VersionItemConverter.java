package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.SelectedItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;

import java.util.List;
import java.util.stream.Collectors;


public class VersionItemConverter {
    public static SelectedItemList<VersionItem> fromResponse(List<VersionResponse> versions, String selectVersionId) {
        return new SelectedItemList<>(fromResponse(versions), selectVersionId);
    }


    public static List<VersionItem> fromResponse(List<VersionResponse> versions) {
        return versions
            .stream()
            .map(VersionItemConverter::fromResponse)
            .collect(Collectors.toList());
    }


    public static VersionItem fromResponse(VersionResponse versionResponse) {
        return new VersionItem(versionResponse.id, versionResponse.gueltigVon, versionResponse.gueltigBis);
    }
}
