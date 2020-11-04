package com.tschanz.v_bro.app.presentation.viewmodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class VersionItem {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");

    private final String id;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;


    public String getId() { return id; }
    public LocalDate getGueltigVon() { return gueltigVon; }
    public LocalDate getGueltigBis() { return gueltigBis; }


    public VersionItem(
        String id,
        LocalDate gueltigVon,
        LocalDate gueltigBis
    ) {
        this.id = id;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
    }


    public String getGueltigVonText() {
        return DATE_FORMATTER.format(this.getGueltigVon());
    }


    public String getGueltigBisText() {
        return DATE_FORMATTER.format(this.getGueltigBis());
    }


    @Override
    public String toString() {
        return this.id + "";
    }
}
