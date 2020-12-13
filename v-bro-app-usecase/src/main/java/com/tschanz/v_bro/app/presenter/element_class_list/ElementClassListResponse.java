package com.tschanz.v_bro.app.presenter.element_class_list;

import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementClassListResponse {
    private final SelectedList<ElementClassResponse> selectedElementList;


    public static ElementClassListResponse fromDomain(SelectedList<ElementClass> elementClassList) {
        var elementClasses = elementClassList.getItems()
            .stream()
            .map(ElementClassResponse::fromDomain)
            .collect(Collectors.toList());
        var selectedElementClass = elementClassList.getSelectedItem() != null
            ? ElementClassResponse.fromDomain(elementClassList.getSelectedItem())
            : null;
        var slist = new SelectedList<>(elementClasses, selectedElementClass);

        return new ElementClassListResponse(slist);
    }
}
