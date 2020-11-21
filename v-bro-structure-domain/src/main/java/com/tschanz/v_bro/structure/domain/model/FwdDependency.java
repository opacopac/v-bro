package com.tschanz.v_bro.structure.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class FwdDependency {
    @Getter protected final String elementClass;
    @Getter protected final String elementId;
    @Getter protected final List<VersionData> versions;
}

