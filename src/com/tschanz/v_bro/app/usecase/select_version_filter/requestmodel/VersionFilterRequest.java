package com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel;

import com.tschanz.v_bro.versions.domain.model.Pflegestatus;

import java.time.LocalDate;


public class VersionFilterRequest {
    public final LocalDate minGueltigVon;
    public final LocalDate maxGueltigBis;
    public final Pflegestatus minPflegestatus;


    public VersionFilterRequest(
        LocalDate minGueltigVon,
        LocalDate maxGueltigBis,
        Pflegestatus minPflegestatus
    ) {
        this.minGueltigVon = minGueltigVon;
        this.maxGueltigBis = maxGueltigBis;
        this.minPflegestatus = minPflegestatus;
    }
}
