package com.tschanz.v_bro.app.presentation.viewmodel.common;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
public class MultiSelectableItemList<T extends IdItem> {
    private final List<T> items;
    private final List<T> selectedItems;


    public MultiSelectableItemList(List<T> items, List<String> selectedItems) {
        this.items = items;
        this.selectedItems = items
            .stream()
            .filter(item -> selectedItems.contains(item.getId()))
            .collect(Collectors.toList());
    }
}
