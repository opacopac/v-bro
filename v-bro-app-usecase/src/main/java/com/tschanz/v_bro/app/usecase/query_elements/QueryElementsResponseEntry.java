package com.tschanz.v_bro.app.usecase.query_elements;

import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class QueryElementsResponseEntry {
    private final String id;
    private final List<String> names;


    public static QueryElementsResponseEntry fromDomain(ElementData elementData) {
        return new QueryElementsResponseEntry(
            elementData.getId(),
            elementData.getNameFieldValues()
                .stream()
                .map(DenominationData::getValue)
                .collect(Collectors.toList())
        );
    }
}
