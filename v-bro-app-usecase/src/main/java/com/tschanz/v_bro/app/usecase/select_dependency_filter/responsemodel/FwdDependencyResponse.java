package com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class FwdDependencyResponse {
    public final String elementClass;
    public final String elementId;
    public final List<VersionResponse> versions;
}
