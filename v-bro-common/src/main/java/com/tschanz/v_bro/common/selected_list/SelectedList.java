package com.tschanz.v_bro.common.selected_list;

import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;


@Getter
public class SelectedList<T> {
    private final List<T> items;
    private final T selectedItem;


    public static <K> SelectedList<K> createEmpty() {
        return new SelectedList<>(Collections.emptyList(), null);
    }


    public SelectedList(@NonNull List<T> items, T selectedItem) {
        if (selectedItem != null && !items.contains(selectedItem)) {
            throw new IllegalArgumentException("selected item must be contained in the item list");
        }

        this.items = items;
        this.selectedItem = selectedItem;
    }
}
