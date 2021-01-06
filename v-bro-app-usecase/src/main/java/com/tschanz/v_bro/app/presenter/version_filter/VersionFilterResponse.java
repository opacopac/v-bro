package com.tschanz.v_bro.app.presenter.version_filter;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Getter
@RequiredArgsConstructor
public class VersionFilterResponse {
    private final LocalDate timelineVon;
    private final LocalDate timelineBis;
    private final Pflegestatus minPflegestatus;


    public static VersionFilterResponse fromDomain(VersionFilter versionFilter) {
        return new VersionFilterResponse(
            versionFilter.getTimelineVon(),
            versionFilter.getTimelineBis(),
            versionFilter.getMinPflegestatus()
        );
    }
}
