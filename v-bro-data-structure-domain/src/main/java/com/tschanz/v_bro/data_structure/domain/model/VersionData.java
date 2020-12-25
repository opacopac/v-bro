package com.tschanz.v_bro.data_structure.domain.model;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;


public class VersionData {
    public static final LocalDate HIGH_DATE = LocalDate.of(9999, 12, 31);
    public static final LocalDate LOW_DATE = LocalDate.of(2000, 1, 1);
    public static final Pflegestatus DEFAULT_PFLEGESTATUS = Pflegestatus.PRODUKTIV;
    public static final String ETERNAL_VERSION_ID = "0";
    @Getter private final ElementData element;
    @Getter private final String id;
    @Getter private final LocalDate gueltigVon;
    @Getter private final LocalDate gueltigBis;
    @Getter private final Pflegestatus pflegestatus;


    public static VersionData createEternal(ElementData elementData) {
        return  new VersionData(elementData, ETERNAL_VERSION_ID);
    }


    public VersionData(ElementData elementData, String id) {
        this(elementData, id, LOW_DATE, HIGH_DATE, DEFAULT_PFLEGESTATUS);
    }


    public VersionData(
        @NonNull ElementData elementData,
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

        this.element = elementData;
        this.id = id;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
        this.pflegestatus = pflegestatus;
    }


    public boolean isEternal() {
        return this.id.equals(ETERNAL_VERSION_ID);
    }
}
