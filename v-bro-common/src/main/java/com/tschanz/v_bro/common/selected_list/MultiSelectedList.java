package com.tschanz.v_bro.common.selected_list;

import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;


@Getter
public class MultiSelectedList<T> {
    private final List<T> items;
    private final List<T> selectedItems;


    public static <K> MultiSelectedList<K> createEmpty() {
        return new MultiSelectedList<>(Collections.emptyList(), Collections.emptyList());
    }


    public MultiSelectedList(@NonNull List<T> items, @NonNull List<T> selectedItems) {
        if (!items.containsAll(selectedItems)) {
            throw new IllegalArgumentException("all selected items must be contained in the item list");
        }

        this.items = items;
        this.selectedItems = selectedItems;
    }
}
