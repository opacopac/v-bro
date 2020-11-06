package com.tschanz.v_bro.app.presentation.viewmodel;

import java.util.List;


public class SelectedItemList<T extends IdItem> {
    private final List<T> items;
    private final T selectedItem;


    public List<T> getItems() { return items; }
    public T getSelectedItem() { return selectedItem; }


    public SelectedItemList(List<T> items, String selectedItem) {
        this.items = items;
        this.selectedItem = items
            .stream()
            .filter(item -> item.getId().equals(selectedItem))
            .findFirst()
            .orElse(null);
    }
}
