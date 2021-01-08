package com.tschanz.v_bro.app.presentation.viewmodel.common;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;


@Getter
public class SelectableItemList<T extends IdItem> {
    private final List<T> items;
    private final T selectedItem;


    public SelectableItemList(@NonNull List<T> items, String selectedItem) {
        this.items = items;
        this.selectedItem = items
            .stream()
            .filter(item -> item.getId().equals(selectedItem))
            .findFirst()
            .orElse(null);
    }
}
