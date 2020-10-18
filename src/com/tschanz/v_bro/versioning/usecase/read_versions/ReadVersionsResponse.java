package com.tschanz.v_bro.versioning.usecase.read_versions;

import java.time.LocalDate;
import java.util.List;


public class ReadVersionsResponse {
    public final List<Version> versions;


    public ReadVersionsResponse(List<Version> versions) {
        this.versions = versions;
    }


    public static class Version {
        public final String id;
        public final LocalDate gueltigVon;
        public final LocalDate gueltigBis;


        public Version(String id, LocalDate gueltigVon, LocalDate gueltigBis) {
            this.id = id;
            this.gueltigVon = gueltigVon;
            this.gueltigBis = gueltigBis;
        }
    }
}
