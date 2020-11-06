package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MultiSelectedItemList;
import com.tschanz.v_bro.app.usecase.common.responsemodel.DenominationResponse;

import java.util.List;
import java.util.stream.Collectors;


public class DenominationItemConverter {
    public static List<String> toRequest(List<DenominationItem> denominations) {
        return denominations
            .stream()
            .map(DenominationItem::getName)
            .collect(Collectors.toList());
    }


    public static MultiSelectedItemList<DenominationItem> fromResponse(List<DenominationResponse> denominations, List<String> selectDenominations) {
        return new MultiSelectedItemList<>(fromResponse(denominations), selectDenominations);
    }


    public static List<DenominationItem> fromResponse(List<DenominationResponse> denominations) {
        return denominations
            .stream()
            .map(DenominationItemConverter::fromResponse)
            .collect(Collectors.toList());
    }


    public static DenominationItem fromResponse(DenominationResponse denominationResponse) {
        return new DenominationItem(denominationResponse.name);
    }
}
