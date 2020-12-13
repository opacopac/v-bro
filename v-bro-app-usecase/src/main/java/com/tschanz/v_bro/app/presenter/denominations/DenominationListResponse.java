package com.tschanz.v_bro.app.presenter.denominations;

import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class DenominationListResponse {
    private final MultiSelectedList<DenominationResponse> denominations;


    public static DenominationListResponse fromDomain(MultiSelectedList<Denomination> denominations) {
        var denominationList = new MultiSelectedList<>(
            denominations.getItems()
                .stream()
                .map(DenominationResponse::fromDomain)
                .collect(Collectors.toList()),
            denominations.getSelectedItems()
                .stream()
                .map(DenominationResponse::fromDomain)
                .collect(Collectors.toList())
        );

        return new DenominationListResponse(denominationList);
    }
}
