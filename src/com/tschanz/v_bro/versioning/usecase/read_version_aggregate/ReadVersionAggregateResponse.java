package com.tschanz.v_bro.versioning.usecase.read_version_aggregate;

import java.time.LocalDate;
import java.util.List;


public class ReadVersionAggregateResponse {
    public final List<VersionAggregateEntry> versionInfoEntries;


    public ReadVersionAggregateResponse(List<VersionAggregateEntry> versionInfoEntries) {
        this.versionInfoEntries = versionInfoEntries;
    }


    public static class VersionAggregateEntry {
        public final String id;
        public final LocalDate gueltigVon;
        public final LocalDate gueltigBis;


        public VersionAggregateEntry(String id, LocalDate gueltigVon, LocalDate gueltigBis) {
            this.id = id;
            this.gueltigVon = gueltigVon;
            this.gueltigBis = gueltigBis;
        }
    }
}
