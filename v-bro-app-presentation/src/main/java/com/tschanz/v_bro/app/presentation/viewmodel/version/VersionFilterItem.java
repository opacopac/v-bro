package com.tschanz.v_bro.app.presentation.viewmodel.version;

import com.tschanz.v_bro.app.presenter.version_filter.VersionFilterResponse;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Getter
@RequiredArgsConstructor
public class VersionFilterItem {
    private final LocalDate minGueltigVon;
    private final LocalDate maxGueltigBis;
    private final Pflegestatus minPflegestatus;


    public static VersionFilterItem fromResponse(VersionFilterResponse response) {
        return new VersionFilterItem(
            response.getTimelineVon(),
            response.getTimelineBis(),
            response.getMinPflegestatus()
        );
    }
}
