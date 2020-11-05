package com.tschanz.v_bro.dependencies.persistence.mock.service;

import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionData;

import java.time.LocalDate;
import java.util.List;


public class MockDependencyService2 implements DependencyService {
    @Override
    public List<FwdDependency> readFwdDependencies(String elemenClass, String elementId, String versionId) throws RepoException {
        return List.of(
            new FwdDependency(
            "P_PRODUKTDEFINITION_E",
            "132242",
                List.of(
                    new VersionData(
                        "444",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 9, 30),
                        Pflegestatus.TEST
                    ),
                    new VersionData(
                        "555",
                        LocalDate.of(2020, 10, 1),
                        LocalDate.of(2022, 12, 31),
                        Pflegestatus.TEST
                    )
                )
            ),
            new FwdDependency(
                "P_PRODUKTDEFINITION_E",
                "1555332",
                List.of(
                    new VersionData(
                        "666",
                        LocalDate.of(2020, 2, 1),
                        LocalDate.of(2020, 2, 28),
                        Pflegestatus.TEST
                    )
                )
            ),
            new FwdDependency(
                "A_LEISTUNGSVERMITTLER",
                "13312",
                List.of(
                    new VersionData(
                        "777",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 3, 31),
                        Pflegestatus.TEST
                    ),
                    new VersionData(
                        "888",
                        LocalDate.of(2020, 10, 1),
                        LocalDate.of(2020, 12, 31),
                        Pflegestatus.TEST
                    ),
                    new VersionData(
                        "999",
                        LocalDate.of(2021, 1, 1),
                        LocalDate.of(9999, 12, 31),
                        Pflegestatus.TEST
                    )
                )
            )
        );
    }
}
