package com.tschanz.v_bro.versions.domain.model;

import java.time.LocalDate;
import java.util.List;


public class VersionFilter {
    public static final VersionFilter DEFAULT_VERSION_FILTER = new VersionFilter(LocalDate.of(2015, 1, 1), VersionData.HIGH_DATE, Pflegestatus.IN_ARBEIT);
    public static final int HD_LD_PADDING_DAYS = 365;
    public static final int DEFAULT_TIMESPAN_DAYS = 5 * 365;
    private final LocalDate minGueltigVon;
    private final LocalDate maxGueltigBis;
    private final Pflegestatus minPflegestatus;


    public LocalDate getMinGueltigVon() { return minGueltigVon; }
    public LocalDate getMaxGueltigBis() { return maxGueltigBis; }
    public Pflegestatus getMinPflegestatus() { return minPflegestatus; }


    public VersionFilter(
        LocalDate minGueltigVon,
        LocalDate maxGueltigBis,
        Pflegestatus minPflegestatus
    ) {
        this.minGueltigVon = minGueltigVon;
        this.maxGueltigBis = maxGueltigBis;
        this.minPflegestatus = minPflegestatus;
    }


    public VersionFilter cropToVersions(List<VersionData> versions) {
        if (versions == null) {
            return null;
        } else if (versions.size() == 0) {
            return new VersionFilter(this.minGueltigVon, this.maxGueltigBis, this.minPflegestatus);
        }

        VersionData firstVersion = versions.get(0);
        VersionData lastVersion = versions.get(versions.size() - 1);
        LocalDate effectiveVon;
        LocalDate effectiveBis;

        // von
        if (this.minGueltigVon.isBefore(firstVersion.getGueltigVon())) {
            effectiveVon = firstVersion.getGueltigVon();
        } else if (this.minGueltigVon.equals(VersionData.LOW_DATE) && firstVersion.getGueltigVon().equals(VersionData.LOW_DATE)) {
            effectiveVon = firstVersion.getGueltigBis().minusDays(HD_LD_PADDING_DAYS);
        } else {
            effectiveVon = this.minGueltigVon;
        }

        // bis
        if (this.maxGueltigBis.isAfter(lastVersion.getGueltigBis())) {
            effectiveBis = lastVersion.getGueltigBis();
        } else if (this.maxGueltigBis.equals(VersionData.HIGH_DATE) && lastVersion.getGueltigBis().equals(VersionData.HIGH_DATE)) {
            effectiveBis = lastVersion.getGueltigVon().plusDays(HD_LD_PADDING_DAYS);
        } else {
            effectiveBis = this.maxGueltigBis;
        }


        return new VersionFilter(
            effectiveVon,
            effectiveBis,
            this.minPflegestatus
        );
    }
}
