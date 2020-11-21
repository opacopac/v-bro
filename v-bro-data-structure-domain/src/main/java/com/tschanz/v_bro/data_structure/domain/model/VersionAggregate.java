package com.tschanz.v_bro.data_structure.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class VersionAggregate {
    @Getter protected final VersionData versionData;
    @Getter protected final AggregateNode rootNode;
}
