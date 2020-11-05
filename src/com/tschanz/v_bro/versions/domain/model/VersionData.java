package com.tschanz.v_bro.versions.domain.model;

import java.time.LocalDate;


public class VersionData {
    public static final LocalDate HIGH_DATE = LocalDate.of(9999, 12, 31);
    public static final LocalDate LOW_DATE = LocalDate.of(2000, 1, 1);
    public static final Pflegestatus DEFAULT_PFLEGESTATUS = Pflegestatus.PRODUKTIV;
    public static final VersionData ETERNAL_VERSION = new VersionData("TODO", LOW_DATE, HIGH_DATE, DEFAULT_PFLEGESTATUS);

    protected final String id;
    protected final LocalDate gueltigVon;
    protected final LocalDate gueltigBis;
    protected final Pflegestatus pflegestatus;


    public String getId() { return this.id; }
    public LocalDate getGueltigVon() { return this.gueltigVon; }
    public LocalDate getGueltigBis() { return this.gueltigBis; }
    public Pflegestatus getPflegestatus() { return pflegestatus; }


    public VersionData(String id) {
        this(id, LOW_DATE, HIGH_DATE, DEFAULT_PFLEGESTATUS);
    }


    public VersionData(
        String id,
        LocalDate gueltigVon,
        LocalDate gueltigBis,
        Pflegestatus pflegestatus
    ) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id must not be null or empty");
        }

        if (gueltigVon == null) {
            throw new IllegalArgumentException("gueltigVon must not be null");
        }

        if (gueltigBis == null) {
            throw new IllegalArgumentException("gueltigBis must not be null");
        }

        if (pflegestatus == null) {
            throw new IllegalArgumentException("pflegestatus must not be null");
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
