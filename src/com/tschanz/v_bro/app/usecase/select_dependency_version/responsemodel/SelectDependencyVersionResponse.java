package com.tschanz.v_bro.app.usecase.select_dependency_version.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.*;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;

import java.util.List;


public class SelectDependencyVersionResponse extends UseCaseResponse {
    public final List<DenominationResponse> denominations;
    public final List<ElementResponse> elements;
    public final List<VersionResponse> versions;
    public final VersionFilterResponse effectiveVersionFilter;
    public final List<FwdDependencyResponse> fwdDependencies;
    public final VersionAggregateResponse versionAggregate;
    public final String selectElementClass;
    public final List<String> selectDenominations;
    public final String selectElementId;
    public final String selectVersionId;


    public SelectDependencyVersionResponse(
        List<DenominationResponse> denominations,
        List<ElementResponse> elements,
        List<VersionResponse> versions,
        VersionFilterResponse effectiveVersionFilter,
        List<FwdDependencyResponse> fwdDependencies,
        VersionAggregateResponse versionAggregate,
        String selectElementClass,
        List<String> selectDenominations,
        String selectElementId,
        String selectVersionId,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.denominations = denominations;
        this.elements = elements;
        this.versions = versions;
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.fwdDependencies = fwdDependencies;
        this.versionAggregate = versionAggregate;
        this.selectElementClass = selectElementClass;
        this.selectDenominations = selectDenominations;
        this.selectElementId = selectElementId;
        this.selectVersionId = selectVersionId;
    }
}
