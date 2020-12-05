package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@RequiredArgsConstructor
public class VersionItem implements IdItem {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");

    private final String id;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;


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
