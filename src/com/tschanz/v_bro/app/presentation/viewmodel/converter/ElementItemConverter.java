package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;

import java.util.List;
import java.util.stream.Collectors;


public class ElementItemConverter {
    public static List<ElementItem> fromResponse(List<ElementResponse> elements) {
        return elements
            .stream()
            .map(element -> new ElementItem(element.id, element.name))
            .collect(Collectors.toList());
    }
}
