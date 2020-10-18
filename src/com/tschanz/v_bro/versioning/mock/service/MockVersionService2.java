package com.tschanz.v_bro.versioning.mock.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versioning.domain.model.FwdDependency;
import com.tschanz.v_bro.versioning.domain.model.VersionData;
import com.tschanz.v_bro.versioning.domain.service.VersionService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public class MockVersionService2 implements VersionService {
    @Override
    public Collection<VersionData> readVersions(String elementName, String elementId) throws RepoException {
        return List.of(
            new VersionData(
                "111",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 5, 31),
                List.of(
                    new FwdDependency("T_PRODUKTDEF_E", "111"),
                    new FwdDependency("T_PRODUKTDEF_E", "222")
                )
            ),
            new VersionData(
                "222",
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2022, 12, 31),
                List.of(
                    new FwdDependency("T_PRODUKTDEF_E", "333")
                )
            ),
            new VersionData(
                "333",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(9999, 12, 31),
                List.of(
                    new FwdDependency("T_PRODUKTDEF_E", "333")
                )
            )
        );
    }
}
