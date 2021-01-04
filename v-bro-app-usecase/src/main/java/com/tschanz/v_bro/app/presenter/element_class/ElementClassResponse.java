package com.tschanz.v_bro.app.presenter.element_class;

import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementClassResponse {
    private final SelectedList<ElementClassResponseItem> selectedElementList;


    public static ElementClassResponse fromDomain(SelectedList<ElementClass> elementClassList) {
        var elementClasses = elementClassList.getItems()
            .stream()
            .map(ElementClassResponseItem::fromDomain)
            .collect(Collectors.toList());
        var selectedElementClass = elementClassList.getSelectedItem() != null
            ? ElementClassResponseItem.fromDomain(elementClassList.getSelectedItem())
            : null;
        var slist = new SelectedList<>(elementClasses, selectedElementClass);

        return new ElementClassResponse(slist);
    }
}
