package com.tschanz.v_bro.app.presentation.viewmodel;

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
}
