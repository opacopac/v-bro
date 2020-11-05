package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;

import java.util.List;
import java.util.stream.Collectors;


public class FwdDependencyItemConverter {
    public static List<FwdDependencyItem> fromResponse(List<FwdDependencyResponse> fwdDependencies) {
        return fwdDependencies
            .stream()
            .map(dependency -> new FwdDependencyItem(
                dependency.elementClass,
                dependency.elementId,
                VersionItemConverter.fromResponse(dependency.versions)
            ))
            .collect(Collectors.toList());
    }
}
