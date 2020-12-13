package com.tschanz.v_bro.app.usecase.select_version_filter;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Getter
@RequiredArgsConstructor
public class SelectVersionFilterRequest {
    private final LocalDate minVon;
    private final LocalDate maxBis;
    private final Pflegestatus minStatus;


    public VersionFilter toDomain() {
        return new VersionFilter(minVon, maxBis, minStatus);
    }
}
