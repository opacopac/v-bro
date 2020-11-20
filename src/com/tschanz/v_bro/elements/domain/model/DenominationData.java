package com.tschanz.v_bro.elements.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;


@RequiredArgsConstructor
public class DenominationData {
    @Getter protected final String name;
    @Getter protected final String value;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenominationData that = (DenominationData) o;
        return Objects.equals(name, that.name);
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
