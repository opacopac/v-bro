package com.tschanz.v_bro.app.usecase.query_element.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;

import java.util.List;


public class QueryElementResponse extends UseCaseResponse {
    public final List<ElementResponse> elements;
    public final long requestTimestamp;


    public QueryElementResponse(
        List<ElementResponse> elements,
        long requestTimestamp,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.elements = elements;
        this.requestTimestamp = requestTimestamp;
    }
}
