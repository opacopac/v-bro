package com.tschanz.v_bro.app.usecase.common.responsemodel;

import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionData;

import java.time.LocalDate;


public class VersionResponse {
    public final String id;
    public final LocalDate gueltigVon;
    public final LocalDate gueltigBis;
    public final Pflegestatus pflegestatus;


    public VersionResponse(
        String id,
        LocalDate gueltigVon,
        LocalDate gueltigBis,
        Pflegestatus pflegestatus
    ) {
        this.id = id;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
        this.pflegestatus = pflegestatus;
    }
}
