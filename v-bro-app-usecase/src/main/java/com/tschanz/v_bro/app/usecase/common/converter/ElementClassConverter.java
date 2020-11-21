package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementClassResponse;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;

import java.util.List;
import java.util.stream.Collectors;


public class ElementClassConverter {
    public static List<ElementClassResponse> toResponse(List<ElementClass> elementClasses) {
        return elementClasses
            .stream()
            .map(elementClass -> new ElementClassResponse(elementClass.getName()))
            .collect(Collectors.toList());
    }
}
