package com.tschanz.v_bro.app.presenter.element;

import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementResponse {
    private final String id;
    private final List<String> denominations;


    public static ElementResponse fromDomain(ElementData elementData) {
        if (elementData == null) {
            return new ElementResponse(null, Collections.emptyList());
        }

        var denominations = elementData.getNameFieldValues()
            .stream()
            .map(DenominationData::getValue)
            .collect(Collectors.toList());

        return new ElementResponse(
            elementData.getId(),
            denominations
        );
    }
}
