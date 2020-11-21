package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.structure.domain.model.Pflegestatus;
import java.time.LocalDate;


public class VersionFilterItem {
    private final LocalDate minGueltiVon;
    private final LocalDate maxGueltigBis;
    private final Pflegestatus minPflegestatus;


    public LocalDate getMinGueltigVon() { return minGueltiVon; }
    public LocalDate getMaxGueltigBis() { return maxGueltigBis; }
    public Pflegestatus getMinPflegestatus() { return minPflegestatus; }


    public VersionFilterItem(LocalDate minGueltiVon, LocalDate maxGueltigBis, Pflegestatus minPflegestatus) {
        this.minGueltiVon = minGueltiVon;
        this.maxGueltigBis = maxGueltigBis;
        this.minPflegestatus = minPflegestatus;
    }
}
