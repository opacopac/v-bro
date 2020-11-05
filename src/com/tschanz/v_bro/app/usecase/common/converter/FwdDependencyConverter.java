package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;

import java.util.List;
import java.util.stream.Collectors;


public class FwdDependencyConverter {
    public static List<FwdDependencyResponse> toResponse(List<FwdDependency> dependencies) {
        return dependencies
            .stream()
            .map(fwdDep -> new FwdDependencyResponse(
                fwdDep.elementName(),
                fwdDep.elementId(),
                VersionConverter.toResponse(fwdDep.getVersions())
            ))
            .collect(Collectors.toList());
    }
}
