package com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;

import java.util.List;


public class SelectDependencyFilterResponse extends UseCaseResponse {
    public final List<FwdDependencyResponse> fwdDependencies;


    public SelectDependencyFilterResponse(
        List<FwdDependencyResponse> fwdDependencies,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.fwdDependencies = fwdDependencies;
    }
}
