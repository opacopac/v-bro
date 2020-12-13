package com.tschanz.v_bro.app.presentation.viewmodel.denominations;

import com.tschanz.v_bro.app.presentation.viewmodel.common.IdItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.denominations.DenominationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class DenominationItem implements IdItem {
    public static final int MAX_DENOMINATIONS = 5;
    private final String name;


    public static List<String> toRequest(List<DenominationItem> denominations) {
        return denominations
            .stream()
            .map(DenominationItem::getName)
            .collect(Collectors.toList());
    }


    public static MultiSelectableItemList<DenominationItem> fromResponse(DenominationListResponse response) {
        var items = response.getDenominations().getItems()
            .stream()
            .map(DenominationItem::fromResponse)
            .collect(Collectors.toList());

        var selectedItems = response.getDenominations().getSelectedItems()
            .stream()
            .map(DenominationItem::fromResponse)
            .map(DenominationItem::getName)
            .collect(Collectors.toList());

        return new MultiSelectableItemList<>(items, selectedItems);
    }


    public static List<DenominationItem> fromResponse(List<DenominationResponse> denominations) {
        return denominations
            .stream()
            .map(DenominationItem::fromResponse)
            .collect(Collectors.toList());
    }


    public static DenominationItem fromResponse(DenominationResponse denomination) {
        return new DenominationItem(denomination.getName());
    }


    @Override
    public String getId() {
        return this.name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
