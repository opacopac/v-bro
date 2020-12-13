package com.tschanz.v_bro.app.state;

import lombok.Getter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
public class MainState {
    private final RepoState repoState = new RepoState();
    private final ElementClassState elementClassState = new ElementClassState();
    private final DenominationState denominationState = new DenominationState();
    private final ElementState elementState = new ElementState();
    private final VersionState versionState = new VersionState();
    private final VersionFilterState versionFilterState = new VersionFilterState();
    private final DependencyState dependencyState = new DependencyState();
    private final VersionAggregateState versionAggregateState = new VersionAggregateState();
}
