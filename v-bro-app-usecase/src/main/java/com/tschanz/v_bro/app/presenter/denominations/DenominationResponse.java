package com.tschanz.v_bro.app.presenter.denominations;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class DenominationResponse {
    private final String name;


    public static DenominationResponse fromDomain(Denomination denomination) {
        return new DenominationResponse(
            denomination.getName()
        );
    }
}
