package com.tschanz.v_bro.app.presentation.viewmodel.denominations;

import com.tschanz.v_bro.app.presentation.viewmodel.common.IdItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.denominations.DenominationResponse;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsRequest;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsRequestItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class DenominationItem implements IdItem {
    private final String path;
    private final String name;


    public static SelectDenominationsRequest toRequest(List<DenominationItem> denominations) {
        var items = denominations
            .stream()
            .map(DenominationItem::toRequest)
            .collect(Collectors.toList());
        return new SelectDenominationsRequest(items);
    }


    public static SelectDenominationsRequestItem toRequest(DenominationItem denomination) {
        return new SelectDenominationsRequestItem(denomination.path, denomination.name);
    }


    public static MultiSelectableItemList<DenominationItem> fromResponse(DenominationListResponse response) {
        var items = response.getDenominations().getItems()
            .stream()
            .map(DenominationItem::fromResponse)
            .collect(Collectors.toList());

        var selectedItems = response.getDenominations().getSelectedItems()
            .stream()
            .map(DenominationItem::fromResponse)
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
        return new DenominationItem(
            denomination.getPath(),
            denomination.getName()
        );
    }


    @Override
    public String getId() {
        return String.format("%s___%s", this.path, this.name);
    }


    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.path);
    }
}
