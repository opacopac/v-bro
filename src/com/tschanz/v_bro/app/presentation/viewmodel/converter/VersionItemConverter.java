package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;

import java.util.List;
import java.util.stream.Collectors;


public class VersionItemConverter {
    public static List<VersionItem> fromResponse(List<VersionResponse> versions) {
        return versions
            .stream()
            .map(version -> new VersionItem(version.id, version.gueltigVon, version.gueltigBis))
            .collect(Collectors.toList());
    }
}
