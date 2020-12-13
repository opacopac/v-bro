package com.tschanz.v_bro.data_structure.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode
@RequiredArgsConstructor
public class Denomination {
    @Getter protected final String name;


    public static List<String> getNameList(List<Denomination> denominations) {
        return denominations
            .stream()
            .map(Denomination::getName)
            .collect(Collectors.toList());
    }
}
