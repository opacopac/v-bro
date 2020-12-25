package com.tschanz.v_bro.data_structure.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@EqualsAndHashCode
@RequiredArgsConstructor
public class ElementData {
    @Getter protected final ElementClass elementClass;
    @Getter protected final String id;
    @Getter protected final List<DenominationData> denominations;
}
