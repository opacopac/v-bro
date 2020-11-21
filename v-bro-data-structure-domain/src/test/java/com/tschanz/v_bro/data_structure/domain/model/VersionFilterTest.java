package com.tschanz.v_bro.data_structure.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class VersionFilterTest {
    @BeforeEach
    void setUp() {
    }


    @Test
    void cropToVersions_multiple_versions_filter_shorter_no_cropping() {
        //     LD                                       HD
        // f:               v            b
        // v:         [--------][---][------------]
        // ->               v            b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2015, 6, 1), LocalDate.of(2020, 6, 1), Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("0", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("1", LocalDate.of(2017, 1, 1), LocalDate.of(2018, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("2", LocalDate.of(2019, 1, 1), LocalDate.of(2020, 12, 31), Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versionFilter.getMinGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versionFilter.getMaxGueltigBis(), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_multiple_versions_filter_shorter_no_cropping_2() {
        //     LD                                       HD
        // f:                     v   b
        // v:        [--------][---------][----------]
        // ->                     v   b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2017, 6, 1), LocalDate.of(2018, 6, 1), Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("0", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("1", LocalDate.of(2017, 1, 1), LocalDate.of(2018, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("2", LocalDate.of(2019, 1, 1), LocalDate.of(2020, 12, 31), Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versionFilter.getMinGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versionFilter.getMaxGueltigBis(), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_one_version_filter_shorter_no_cropping() {
        //     LD                                       HD
        // f:               v            b
        // v:         [--------------------------]
        // ->               v            b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2015, 6, 1), LocalDate.of(2020, 6, 1), Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("1", LocalDate.of(2015, 1, 1), LocalDate.of(2020, 12, 31), Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versionFilter.getMinGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versionFilter.getMaxGueltigBis(), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_no_version_no_cropping() {
        //     LD                                       HD
        // f:               v            b
        // v:                     nix
        // ->               v            b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2015, 6, 1), LocalDate.of(2020, 6, 1), Pflegestatus.PRODUKTIV);
        List<VersionData> versions = Collections.emptyList();

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertNotNull(croppedFilter);
        assertEquals(versionFilter.getMinGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versionFilter.getMaxGueltigBis(), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_filter_longer_before_cropping() {
        //     LD                                            HD
        // f:        v                        b
        // v:             [--------][---][---------]
        // ->             v                   b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2013, 6, 1), LocalDate.of(2020, 6, 1), Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("0", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("1", LocalDate.of(2017, 1, 1), LocalDate.of(2018, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("2", LocalDate.of(2019, 1, 1), LocalDate.of(2020, 12, 31), Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versions.get(0).getGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versionFilter.getMaxGueltigBis(), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_filter_longer_after_cropping() {
        //     LD                                            HD
        // f:                  v                        b
        // v:             [--------][---][---------]
        // ->                  v                   b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2015, 6, 1), LocalDate.of(2022, 6, 1), Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("0", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("1", LocalDate.of(2017, 1, 1), LocalDate.of(2018, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("2", LocalDate.of(2019, 1, 1), LocalDate.of(2020, 12, 31), Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versionFilter.getMinGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versions.get(2).getGueltigBis(), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_filter_longer_both_sides_cropping() {
        //     LD                                            HD
        // f:        v                                  b
        // v:             [--------][---][---------]
        // ->             v                        b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2013, 6, 1), LocalDate.of(2022, 6, 1), Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("0", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("1", LocalDate.of(2017, 1, 1), LocalDate.of(2018, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("2", LocalDate.of(2019, 1, 1), LocalDate.of(2020, 12, 31), Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versions.get(0).getGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versions.get(2).getGueltigBis(), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_filter_and_version_till_hd_cropping_and_padding_end() {
        //     LD                                            HD
        // f:                 v                               b
        // v:             [--------][---][--------------------]
        // ->                 v          <padding>b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2015, 6, 1), VersionData.HIGH_DATE, Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("0", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("1", LocalDate.of(2017, 1, 1), LocalDate.of(2018, 12, 31), Pflegestatus.PRODUKTIV),
            new VersionData("2", LocalDate.of(2019, 1, 1), VersionData.HIGH_DATE, Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versionFilter.getMinGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versions.get(2).getGueltigVon().plusDays(VersionFilter.HD_LD_PADDING_DAYS), croppedFilter.getMaxGueltigBis());
    }


    @Test
    void cropToVersions_filter_and_version_from_lw_till_hd_TODO() {
        //     LD                                            HD
        // f:                 v                               b
        // v:  [----------------------------------------------]
        // ->                 v<default timespan>b
        VersionFilter versionFilter = new VersionFilter(LocalDate.of(2015, 6, 1), VersionData.HIGH_DATE, Pflegestatus.PRODUKTIV);
        List<VersionData> versions = List.of(
            new VersionData("0", VersionData.LOW_DATE, VersionData.HIGH_DATE, Pflegestatus.PRODUKTIV)
        );

        VersionFilter croppedFilter = versionFilter.cropToVersions(versions);

        assertEquals(versionFilter.getMinGueltigVon(), croppedFilter.getMinGueltigVon());
        assertEquals(versionFilter.getMinGueltigVon().plusDays(VersionFilter.DEFAULT_TIMESPAN_DAYS), croppedFilter.getMaxGueltigBis());
    }
}
