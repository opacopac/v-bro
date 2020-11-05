package com.tschanz.v_bro.versions.persistence.mock.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versions.domain.model.*;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.time.LocalDate;
import java.util.List;


public class MockVersionService2 implements VersionService {
    @Override
    public List<VersionData> readVersionTimeline(String elementClass, String elementId) throws RepoException {
        return List.of(
            new VersionData(
                "111",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 5, 31),
                Pflegestatus.TEST
            ),
            new VersionData(
                "222",
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2022, 12, 31),
                Pflegestatus.TEST
            ),
            new VersionData(
                "333",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(9999, 12, 31),
                Pflegestatus.TEST
            )
        );
    }
}
