package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionFilterResponse;


public class VersionFilterItemConverter {
    public static VersionFilterRequest toRequest(VersionFilterItem versionFilter) {
        return new VersionFilterRequest(
            versionFilter.getMinGueltigVon(),
            versionFilter.getMaxGueltigBis(),
            versionFilter.getMinPflegestatus()
        );
    }


    public static VersionFilterItem fromResponse(VersionFilterResponse versionFilter) {
        return new VersionFilterItem(
            versionFilter.minGueltigVon,
            versionFilter.maxGueltigBis,
            versionFilter.minPflegestatus
        );
    }
}
