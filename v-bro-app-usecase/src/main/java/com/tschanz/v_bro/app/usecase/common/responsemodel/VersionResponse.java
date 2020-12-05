package com.tschanz.v_bro.app.usecase.common.responsemodel;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@RequiredArgsConstructor
public class VersionResponse {
    public final String id;
    public final LocalDate gueltigVon;
    public final LocalDate gueltigBis;
    public final Pflegestatus pflegestatus;
}
