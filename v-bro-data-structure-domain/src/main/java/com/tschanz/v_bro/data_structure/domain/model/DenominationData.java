package com.tschanz.v_bro.data_structure.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@EqualsAndHashCode
@RequiredArgsConstructor
public class DenominationData {
    @Getter protected final String name;
    @Getter protected final String value;
}
