package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;


public class MockVersionService implements VersionService {
    @Override
    public List<VersionData> readVersions(
        @NonNull ElementData element,
        @NonNull LocalDate timelineVon,
        @NonNull LocalDate timelineBis,
        @NonNull Pflegestatus minPflegestatus
    ) throws RepoException {
        return List.of(
            new VersionData(
                element,
                "111",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 5, 31),
                Pflegestatus.TEST
            ),
            new VersionData(
                element,
                "222",
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2022, 12, 31),
                Pflegestatus.TEST
            ),
            new VersionData(
                element,
                "333",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(9999, 12, 31),
                Pflegestatus.TEST
            )
        );
    }
}
