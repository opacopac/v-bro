package com.tschanz.v_bro.elements.presentation.viewmodel;

import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import java.time.LocalDate;


public class VersionFilter {
    private final LocalDate minGueltiVon;
    private final LocalDate maxGueltigBis;
    private final Pflegestatus minPflegestatus;


    public LocalDate getMinGueltiVon() { return minGueltiVon; }
    public LocalDate getMaxGueltigBis() { return maxGueltigBis; }
    public Pflegestatus getMinPflegestatus() { return minPflegestatus; }


    public VersionFilter(LocalDate minGueltiVon, LocalDate maxGueltigBis, Pflegestatus minPflegestatus) {
        this.minGueltiVon = minGueltiVon;
        this.maxGueltigBis = maxGueltigBis;
        this.minPflegestatus = minPflegestatus;
    }
}
