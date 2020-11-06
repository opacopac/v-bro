package com.tschanz.v_bro.app.presentation.viewmodel;

import java.util.List;
import java.util.stream.Collectors;


public class MultiSelectedItemList<T extends IdItem> {
    private final List<T> items;
    private final List<T> selectedItems;


    public List<T> getItems() { return items; }
    public List<T> getSelectedItems() { return selectedItems; }


    public MultiSelectedItemList(List<T> items, List<String> selectedItems) {
        this.items = items;
        this.selectedItems = items
            .stream()
            .filter(item -> selectedItems.contains(item.getId()))
            .collect(Collectors.toList());
    }
}
