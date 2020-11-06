package com.tschanz.v_bro.app.usecase.select_element_class.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.*;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;

import java.util.List;


public class SelectElementClassResponse extends UseCaseResponse {
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


    public SelectElementClassResponse(
        List<DenominationResponse> denominations,
        List<ElementResponse> elements,
        List<VersionResponse> versions,
        VersionFilterResponse effectiveVersionFilter,
        List<FwdDependencyResponse> fwdDependencies,
        VersionAggregateResponse versionAggregate,
        List<String> selectDenominations,
        String selectElementClass,
        String selectElementId,
        String selectVersionId,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.denominations = denominations;
        this.elements = elements;
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.versions = versions;
        this.fwdDependencies = fwdDependencies;
        this.versionAggregate = versionAggregate;
        this.selectElementClass = selectElementClass;
        this.selectDenominations = selectDenominations;
        this.selectElementId = selectElementId;
        this.selectVersionId = selectVersionId;
    }
}
