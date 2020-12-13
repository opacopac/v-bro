package com.tschanz.v_bro.app.presentation.viewmodel.element_class;

import com.tschanz.v_bro.app.presentation.viewmodel.common.IdItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListResponse;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementClassItem implements IdItem {
    private final String name;


    public static SelectableItemList<ElementClassItem> fromResponse(ElementClassListResponse response) {
        var elements = response.getSelectedElementList().getItems()
            .stream()
            .map(ElementClassItem::fromResponse)
            .collect(Collectors.toList());
        var selectedElementName = response.getSelectedElementList().getSelectedItem() != null
            ? response.getSelectedElementList().getSelectedItem().getName()
            : null;

        return new SelectableItemList<>(elements, selectedElementName);
    }


    public static ElementClassItem fromResponse(ElementClassResponse elementClassResponse) {
        return new ElementClassItem(elementClassResponse.getName());
    }


    @Override
    public String getId() {
        return this.name;
    }


    @Override
    public String toString() {
        return this.getName();
    }
}
