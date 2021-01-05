package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class MockDependencyService implements DependencyService {
    @Override
    public List<Dependency> readFwdDependencies(
        @NonNull VersionData version,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        int maxResults
    ) {
        var elementClass1 = new ElementClass("P_PRODUKTDEFINITION_E");
        var element1 = new ElementData(elementClass1, "132242", Collections.emptyList());
        var elementClass2 = new ElementClass("P_PRODUKTDEFINITION_E");
        var element2 = new ElementData(elementClass1, "1555332", Collections.emptyList());
        var elementClass3 = new ElementClass("A_LEISTUNGSVERMITTLER");
        var element3 = new ElementData(elementClass1, "13312", Collections.emptyList());

        return List.of(
            new Dependency(
            elementClass1,
            element1,
                List.of(
                    new VersionData(
                        element1,
                        "444",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 9, 30),
                        Pflegestatus.TEST
                    ),
                    new VersionData(
                        element1,
                        "555",
                        LocalDate.of(2020, 10, 1),
                        LocalDate.of(2022, 12, 31),
                        Pflegestatus.TEST
                    )
                )
            ),
            new Dependency(
                elementClass2,
                element2,
                List.of(
                    new VersionData(
                        element2,
                        "666",
                        LocalDate.of(2020, 2, 1),
                        LocalDate.of(2020, 2, 28),
                        Pflegestatus.TEST
                    )
                )
            ),
            new Dependency(
                elementClass3,
                element3,
                List.of(
                    new VersionData(
                        element3,
                        "777",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 3, 31),
                        Pflegestatus.TEST
                    ),
                    new VersionData(
                        element3,
                        "888",
                        LocalDate.of(2020, 10, 1),
                        LocalDate.of(2020, 12, 31),
                        Pflegestatus.TEST
                    ),
                    new VersionData(
                        element3,
                        "999",
                        LocalDate.of(2021, 1, 1),
                        LocalDate.of(9999, 12, 31),
                        Pflegestatus.TEST
                    )
                )
            )
        );
    }


    @Override
    public List<Dependency> readBwdDependencies(
        @NonNull ElementData element,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        int maxResults
    ) {
        var elementClass1 = new ElementClass("P_PRODUKTDEFINITION_E");
        var element1 = new ElementData(elementClass1, "132242", Collections.emptyList());

        return List.of(
            new Dependency(
                elementClass1,
                element1,
                List.of(
                    new VersionData(
                        element1,
                        "444",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 9, 30),
                        Pflegestatus.TEST
                    ),
                    new VersionData(
                        element1,
                        "555",
                        LocalDate.of(2020, 10, 1),
                        LocalDate.of(2022, 12, 31),
                        Pflegestatus.TEST
                    )
                )
            )
        );
    }
}
