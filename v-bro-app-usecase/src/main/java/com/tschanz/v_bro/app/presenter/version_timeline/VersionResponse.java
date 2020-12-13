package com.tschanz.v_bro.app.presenter.version_timeline;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class VersionResponse {
    private final String id;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final Pflegestatus pflegestatus;


    public static VersionResponse fromVersionData(VersionData versionData) {
        return new VersionResponse(
            versionData.getId(),
            versionData.getGueltigVon(),
            versionData.getGueltigBis(),
            versionData.getPflegestatus()
        );
    }
}
