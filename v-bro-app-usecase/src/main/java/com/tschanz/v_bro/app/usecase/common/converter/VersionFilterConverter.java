package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionFilterResponse;
import com.tschanz.v_bro.data_structure.domain.model.VersionFilter;


public class VersionFilterConverter {
    public static VersionFilterResponse toResponse(VersionFilter versionFilter) {
        if (versionFilter != null) {
            return new VersionFilterResponse(
                versionFilter.getMinGueltigVon(),
                versionFilter.getMaxGueltigBis(),
                versionFilter.getMinPflegestatus()
            );
        } else {
            return null;
        }
    }


    public static VersionFilter fromRequest(VersionFilterRequest versionFilterRequest) {
        return new VersionFilter(
            versionFilterRequest.minGueltigVon,
            versionFilterRequest.maxGueltigBis,
            versionFilterRequest.minPflegestatus
        );
    }
}
