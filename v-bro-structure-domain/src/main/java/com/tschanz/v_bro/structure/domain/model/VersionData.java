package com.tschanz.v_bro.structure.domain.model;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;


public class VersionData {
    public static final LocalDate HIGH_DATE = LocalDate.of(9999, 12, 31);
    public static final LocalDate LOW_DATE = LocalDate.of(2000, 1, 1);
    public static final Pflegestatus DEFAULT_PFLEGESTATUS = Pflegestatus.PRODUKTIV;
    public static final VersionData ETERNAL_VERSION = new VersionData("0", LOW_DATE, HIGH_DATE, DEFAULT_PFLEGESTATUS);
    @Getter protected final String id;
    @Getter protected final LocalDate gueltigVon;
    @Getter protected final LocalDate gueltigBis;
    @Getter protected final Pflegestatus pflegestatus;


    public VersionData(String id) {
        this(id, LOW_DATE, HIGH_DATE, DEFAULT_PFLEGESTATUS);
    }


    public VersionData(
        @NonNull String id,
        @NonNull LocalDate gueltigVon,
        @NonNull LocalDate gueltigBis,
        @NonNull Pflegestatus pflegestatus
    ) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("id must not be empty");
        }

        if (gueltigVon.isAfter(gueltigBis)) {
            throw  new IllegalArgumentException("gueltigVon must be smaller or equal to gueltigBis");
        }

        this.id = id;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
        this.pflegestatus = pflegestatus;
    }
}
