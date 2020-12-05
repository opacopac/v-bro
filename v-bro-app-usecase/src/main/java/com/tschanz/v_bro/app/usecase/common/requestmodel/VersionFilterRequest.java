package com.tschanz.v_bro.app.usecase.common.requestmodel;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@RequiredArgsConstructor
public class VersionFilterRequest {
    public final LocalDate minGueltigVon;
    public final LocalDate maxGueltigBis;
    public final Pflegestatus minPflegestatus;
}
