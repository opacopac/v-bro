package com.tschanz.v_bro.app.usecase.select_element.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionAggregateResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionFilterResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;

import java.util.List;


public class SelectElementResponse extends UseCaseResponse {
    public final List<VersionResponse> versions;
    public final VersionFilterResponse effectiveVersionFilter;
    public final List<FwdDependencyResponse> fwdDependencies;
    public final VersionAggregateResponse versionAggregate;
    public final String selectElementId;
    public final String selectVersionId;


    public SelectElementResponse(
        List<VersionResponse> versions,
        VersionFilterResponse effectiveVersionFilter,
        List<FwdDependencyResponse> fwdDependencies,
        VersionAggregateResponse versionAggregate,
        String selectElementId,
        String selectVersionId,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.versions = versions;
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.fwdDependencies = fwdDependencies;
        this.versionAggregate = versionAggregate;
        this.selectElementId = selectElementId;
        this.selectVersionId = selectVersionId;
    }
}
