package com.tschanz.v_bro.app.presentation.viewmodel.element_class;

import com.tschanz.v_bro.app.presentation.viewmodel.common.IdItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassResponse;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassResponseItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementClassItem implements IdItem {
    private final String name;


    public static SelectableItemList<ElementClassItem> fromResponse(ElementClassResponse response) {
        var elements = response.getSelectedElementList().getItems()
            .stream()
            .map(ElementClassItem::fromResponse)
            .collect(Collectors.toList());
        var selectedElementName = response.getSelectedElementList().getSelectedItem() != null
            ? response.getSelectedElementList().getSelectedItem().getName()
            : null;

        return new SelectableItemList<>(elements, selectedElementName);
    }


    public static ElementClassItem fromResponse(ElementClassResponseItem elementClassResponseItem) {
        return new ElementClassItem(elementClassResponseItem.getName());
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
