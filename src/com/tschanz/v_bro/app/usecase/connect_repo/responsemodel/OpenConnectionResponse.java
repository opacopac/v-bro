package com.tschanz.v_bro.app.usecase.connect_repo.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.*;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;

import java.util.List;


public class OpenConnectionResponse extends UseCaseResponse {
    public final RepoConnectionResponse repoConnection;
    public final List<ElementClassResponse> elementClasses;
    public final List<DenominationResponse> denominations;
    public final List<ElementResponse> elements;
    public final List<VersionResponse> versions;
    public final VersionFilterResponse versionFilter;
    public final VersionFilterResponse effectiveVersionFilter;
    public final List<FwdDependencyResponse> fwdDependencies;
    public final VersionAggregateResponse versionAggregate;
    public final String selectElementClass;
    public final List<String> selectDenominations;
    public final String selectElementId;
    public final String selectVersionId;


    public OpenConnectionResponse(
        RepoConnectionResponse repoConnection,
        List<ElementClassResponse> elementClasses,
        List<DenominationResponse> denominations,
        List<ElementResponse> elements,
        List<VersionResponse> versions,
        VersionFilterResponse versionFilter,
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
        this.repoConnection = repoConnection;
        this.elementClasses = elementClasses;
        this.denominations = denominations;
        this.elements = elements;
        this.versions = versions;
        this.versionFilter = versionFilter;
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.fwdDependencies = fwdDependencies;
        this.versionAggregate = versionAggregate;
        this.selectElementClass = selectElementClass;
        this.selectDenominations = selectDenominations;
        this.selectElementId = selectElementId;
        this.selectVersionId = selectVersionId;
    }
}
