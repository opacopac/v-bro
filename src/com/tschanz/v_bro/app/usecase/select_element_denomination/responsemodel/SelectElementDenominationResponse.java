package com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;

import java.util.List;


public class SelectElementDenominationResponse extends UseCaseResponse {
    public final List<ElementResponse> elements;


    public SelectElementDenominationResponse(
        List<ElementResponse> elements,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.elements = elements;
    }
}
