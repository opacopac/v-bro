package com.tschanz.v_bro.app.usecase.select_element_class.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.DenominationResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;

import java.util.List;


public class SelectElementClassResponse extends UseCaseResponse {
    public final List<DenominationResponse> denominations;
    public final List<ElementResponse> elements;


    public SelectElementClassResponse(
        List<DenominationResponse> denominations,
        List<ElementResponse> elements,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.denominations = denominations;
        this.elements = elements;
    }
}
