package com.tschanz.v_bro.app.presentation.viewmodel.element;

import com.tschanz.v_bro.app.presentation.viewmodel.common.IdItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presenter.element_list.ElementListResponse;
import com.tschanz.v_bro.app.presenter.element_list.ElementResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementItem implements IdItem {
    private static final int MAX_NAME_LENGTH = 100;
    private final String id;
    private final String name;


    public static SelectableItemList<ElementItem> fromResponse(ElementListResponse response) {
        var items = response.getSelectedElementList().getItems()
            .stream()
            .map(ElementItem::fromResponse)
            .collect(Collectors.toList());
        var selectedId = response.getSelectedElementList().getSelectedItem() != null
            ? response.getSelectedElementList().getSelectedItem().getId()
            : null;

        return new SelectableItemList<>(items, selectedId);
    }


    public static ElementItem fromResponse(ElementResponse element) {
        var concatName = String.join(" - ", element.getNames());

        return new ElementItem(element.getId(), concatName);
    }


    @Override
    public String toString() {
        if (this.name.length() <= MAX_NAME_LENGTH) {
            return this.name;
        } else {
            return this.name.substring(0, MAX_NAME_LENGTH) + "...";
        }
    }
}
