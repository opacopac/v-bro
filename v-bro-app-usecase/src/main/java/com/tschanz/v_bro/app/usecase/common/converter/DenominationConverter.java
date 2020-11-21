package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.common.responsemodel.DenominationResponse;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;

import java.util.List;
import java.util.stream.Collectors;


public class DenominationConverter {
    public static List<DenominationResponse> toResponse(List<Denomination> denominations) {
        return denominations
            .stream()
            .map(Denomination::getName)
            .map(DenominationResponse::new)
            .collect(Collectors.toList());
    }
}
