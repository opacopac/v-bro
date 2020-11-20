package com.tschanz.v_bro.element_classes.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class Denomination {
    @Getter protected final String name;



    public static List<String> getNameList(List<Denomination> denominations) {
        return denominations
            .stream()
            .map(Denomination::getName)
            .collect(Collectors.toList());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Denomination that = (Denomination) o;
        return Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
