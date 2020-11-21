package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.structure.domain.model.VersionData;

import java.util.List;
import java.util.stream.Collectors;


public class VersionConverter {
    public static VersionResponse toResponse(VersionData versionData) {
        return new VersionResponse(
            versionData.getId(),
            versionData.getGueltigVon(),
            versionData.getGueltigBis(),
            versionData.getPflegestatus()
        );
    }


    public static List<VersionResponse> toResponse(List<VersionData> versions) {
        return versions
            .stream()
            .map(VersionConverter::toResponse)
            .collect(Collectors.toList());
    }
}
