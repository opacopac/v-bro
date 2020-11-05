package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementClassResponse;

import java.util.List;
import java.util.stream.Collectors;


public class ElementClassItemConverter {
    public static List<ElementClassItem> fromResponse(List<ElementClassResponse> elementClasses) {
        return elementClasses
            .stream()
            .map(elementClass -> new ElementClassItem(elementClass.name))
            .collect(Collectors.toList());
    }
}
