package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;
import com.tschanz.v_bro.structure.domain.model.DenominationData;
import com.tschanz.v_bro.structure.domain.model.ElementData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ElementConverter {
    public static List<ElementResponse> toResponse(List<ElementData> elements) {
        return elements
            .stream()
            .map(element -> new ElementResponse(element.getId(), getElementName(element)))
            .sorted(Comparator.comparing(e -> e.name)) // TODO: move to service?
            .collect(Collectors.toList());
    }


    // TODO: move to service?
    private static String getElementName(ElementData element) {
        if (element.getNameFieldValues().size() == 0) {
            return element.getId();
        } else {
            return element.getNameFieldValues()
                .stream()
                .map(DenominationData::getValue)
                .collect(Collectors.joining(" - "));
        }
    }
}
