package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.*;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;


public interface DependencyService {
    List<Dependency> readFwdDependencies(
        @NonNull VersionData version,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        int maxResults
        );

    List<Dependency> readBwdDependencies(
        @NonNull ElementData element,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        int maxResults
    );
}
