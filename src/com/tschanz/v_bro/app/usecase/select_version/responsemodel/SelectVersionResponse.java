package com.tschanz.v_bro.app.usecase.select_version.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionAggregateResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;

import java.util.List;


public class SelectVersionResponse extends UseCaseResponse {
    public final List<FwdDependencyResponse> fwdDependencies;
    public final VersionAggregateResponse versionAggregate;
    public final String selectVersionId;


    public SelectVersionResponse(
        List<FwdDependencyResponse> fwdDependencies,
        VersionAggregateResponse versionAggregate,
        String selectVersionId,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.fwdDependencies = fwdDependencies;
        this.versionAggregate = versionAggregate;
        this.selectVersionId = selectVersionId;
    }
}
