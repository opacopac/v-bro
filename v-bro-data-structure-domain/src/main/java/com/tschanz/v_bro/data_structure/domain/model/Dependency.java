package com.tschanz.v_bro.data_structure.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class Dependency {
    @Getter protected final ElementClass elementClass;
    @Getter protected final ElementData element;
    @Getter protected final List<VersionData> versions;
}
