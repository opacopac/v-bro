package com.tschanz.v_bro.app.usecase.common.responsemodel;

import com.tschanz.v_bro.versions.domain.model.Pflegestatus;

import java.time.LocalDate;


public class VersionFilterResponse {
    public final LocalDate minGueltigVon;
    public final LocalDate maxGueltigBis;
    public final Pflegestatus minPflegestatus;


    public VersionFilterResponse(
        LocalDate minGueltigVon,
        LocalDate maxGueltigBis,
        Pflegestatus minPflegestatus
    ) {
        this.minGueltigVon = minGueltigVon;
        this.maxGueltigBis = maxGueltigBis;
        this.minPflegestatus = minPflegestatus;
    }
}
