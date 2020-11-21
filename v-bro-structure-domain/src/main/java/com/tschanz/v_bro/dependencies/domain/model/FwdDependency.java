package com.tschanz.v_bro.dependencies.domain.model;

import com.tschanz.v_bro.versions.domain.model.VersionData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class FwdDependency {
    @Getter protected final String elementClass;
    @Getter protected final String elementId;
    @Getter protected final List<VersionData> versions;
}

