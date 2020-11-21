package com.tschanz.v_bro.app.usecase.common.requestmodel;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;

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
