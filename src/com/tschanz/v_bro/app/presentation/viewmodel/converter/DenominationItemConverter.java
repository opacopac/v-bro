package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;
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


    public static List<DenominationItem> fromResponse(List<DenominationResponse> denominations) {
        return denominations
            .stream()
            .map(denomination -> new DenominationItem(denomination.name))
            .collect(Collectors.toList());
    }
}
