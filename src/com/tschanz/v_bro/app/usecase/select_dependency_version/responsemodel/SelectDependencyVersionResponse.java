package com.tschanz.v_bro.app.usecase.select_dependency_version.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.*;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;

import java.util.List;


public class SelectDependencyVersionResponse extends UseCaseResponse {
    public final String selectElementClass;
    public final String selectElementId;
    public final String selectVersionId;
    public final List<DenominationResponse> denominations;
    public final List<ElementResponse> elements;
    public final VersionFilterResponse effectiveVersionFilter;
    public final List<VersionResponse> versions;
    public final List<FwdDependencyResponse> fwdDependencies;
    public final VersionAggregateResponse versionAggregate;


    public SelectDependencyVersionResponse(
        String selectElementClass,
        String selectElementId,
        String selectVersionId,
        List<DenominationResponse> denominations,
        List<ElementResponse> elements,
        VersionFilterResponse effectiveVersionFilter,
        List<VersionResponse> versions,
        List<FwdDependencyResponse> fwdDependencies,
        VersionAggregateResponse versionAggregate,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.selectElementClass = selectElementClass;
        this.selectElementId = selectElementId;
        this.selectVersionId = selectVersionId;
        this.denominations = denominations;
        this.elements = elements;
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.versions = versions;
        this.fwdDependencies = fwdDependencies;
        this.versionAggregate = versionAggregate;
    }
}
