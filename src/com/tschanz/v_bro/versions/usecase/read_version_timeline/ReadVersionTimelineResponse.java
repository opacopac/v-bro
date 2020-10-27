package com.tschanz.v_bro.versions.usecase.read_version_timeline;

import java.time.LocalDate;
import java.util.List;


public class ReadVersionTimelineResponse {
    public final List<VersionInfoEntry> versionInfoEntries;


    public ReadVersionTimelineResponse(List<VersionInfoEntry> versionInfoEntries) {
        this.versionInfoEntries = versionInfoEntries;
    }


    public static class VersionInfoEntry {
        public final String id;
        public final LocalDate gueltigVon;
        public final LocalDate gueltigBis;


        public VersionInfoEntry(String id, LocalDate gueltigVon, LocalDate gueltigBis) {
            this.id = id;
            this.gueltigVon = gueltigVon;
            this.gueltigBis = gueltigBis;
        }
    }
}
