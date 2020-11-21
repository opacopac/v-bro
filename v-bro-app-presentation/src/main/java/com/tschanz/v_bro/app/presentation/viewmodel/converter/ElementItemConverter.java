package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectedItemList;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;

import java.util.List;
import java.util.stream.Collectors;


public class ElementItemConverter {
    public static SelectedItemList<ElementItem> fromResponse(List<ElementResponse> elements, String selectElementId) {
        return new SelectedItemList<>(fromResponse(elements), selectElementId);
    }


    public static List<ElementItem> fromResponse(List<ElementResponse> elements) {
        return elements
            .stream()
            .map(ElementItemConverter::fromResponse)
            .collect(Collectors.toList());
    }


    public static ElementItem fromResponse(ElementResponse elementResponse) {
        return new ElementItem(elementResponse.id, elementResponse.name);
    }
}
