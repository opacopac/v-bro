package com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;

import java.util.List;


public class FwdDependencyResponse {
    public final String elementClass;
    public final String elementId;
    public final List<VersionResponse> versions;


    public FwdDependencyResponse(
        String elementClass,
        String elementId,
        List<VersionResponse> versions
    ) {
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versions = versions;
    }
}
