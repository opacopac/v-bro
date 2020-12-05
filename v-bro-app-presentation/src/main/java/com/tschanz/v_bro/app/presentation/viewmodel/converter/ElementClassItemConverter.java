package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementClassResponse;

import java.util.List;
import java.util.stream.Collectors;


public class ElementClassItemConverter {
    public static SelectableItemList<ElementClassItem> fromResponse(List<ElementClassResponse> elementClasses, String selectElementClass) {
        return new SelectableItemList<>(fromResponse(elementClasses), selectElementClass);
    }


    public static List<ElementClassItem> fromResponse(List<ElementClassResponse> elementClasses) {
        return elementClasses
            .stream()
            .map(ElementClassItemConverter::fromResponse)
            .collect(Collectors.toList());
    }


    public static ElementClassItem fromResponse(ElementClassResponse elementClassResponse) {
        return new ElementClassItem(elementClassResponse.name);
    }
}
