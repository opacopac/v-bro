package com.tschanz.v_bro.data_structure.domain.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class VersionFilter {
    public static final VersionFilter DEFAULT_VERSION_FILTER = new VersionFilter(
        LocalDate.now().minusYears(3),
        LocalDate.now().plusYears(1),
        Pflegestatus.IN_ARBEIT
    );
    public static final int HD_LD_PADDING_DAYS = 365;
    public static final int DEFAULT_TIMESPAN_DAYS = 5 * 365;
    @NonNull private final LocalDate timelineVon;
    @NonNull private final LocalDate timelineBis;
    @NonNull private final Pflegestatus minPflegestatus;


    public VersionFilter cropToVersions(List<VersionData> versions) {
        if (versions == null) {
            return null;
        } else if (versions.isEmpty()) {
            return new VersionFilter(this.timelineVon, this.timelineBis, this.minPflegestatus);
        }

        VersionData firstVersion = versions.get(0);
        VersionData lastVersion = versions.get(versions.size() - 1);
        LocalDate effectiveVon;
        LocalDate effectiveBis;

        // von
        if (this.timelineVon.isBefore(firstVersion.getGueltigVon())) {
            effectiveVon = firstVersion.getGueltigVon();
        } else if (this.timelineVon.equals(VersionData.LOW_DATE) && firstVersion.getGueltigVon().equals(VersionData.LOW_DATE)) {
            effectiveVon = firstVersion.getGueltigBis().minusDays(HD_LD_PADDING_DAYS);
        } else {
            effectiveVon = this.timelineVon;
        }

        // bis
        if (this.timelineBis.isAfter(lastVersion.getGueltigBis())) {
            effectiveBis = lastVersion.getGueltigBis();
        } else if (this.timelineBis.equals(VersionData.HIGH_DATE) && lastVersion.getGueltigBis().equals(VersionData.HIGH_DATE)) {
            effectiveBis = lastVersion.getGueltigVon().plusDays(HD_LD_PADDING_DAYS);
        } else {
            effectiveBis = this.timelineBis;
        }


        return new VersionFilter(
            effectiveVon,
            effectiveBis,
            this.minPflegestatus
        );
    }
}
