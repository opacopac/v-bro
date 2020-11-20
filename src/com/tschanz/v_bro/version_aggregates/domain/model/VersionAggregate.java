package com.tschanz.v_bro.version_aggregates.domain.model;

import com.tschanz.v_bro.versions.domain.model.VersionData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionAggregate {
    @Getter protected final VersionData versionData;
    @Getter protected final AggregateNode rootNode;
}
