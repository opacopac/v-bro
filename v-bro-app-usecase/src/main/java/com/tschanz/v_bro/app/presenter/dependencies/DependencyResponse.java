package com.tschanz.v_bro.app.presenter.dependencies;

import com.tschanz.v_bro.app.presenter.version_timeline.VersionResponse;
import com.tschanz.v_bro.data_structure.domain.model.FwdDependency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class DependencyResponse {
    private final String elementClass;
    private final String elementId;
    private final List<VersionResponse> versions;


    public static DependencyResponse fromDependency(FwdDependency fwdDependency) {
        return new DependencyResponse(
            fwdDependency.getElementClass().getName(),
            fwdDependency.getElement().getId(),
            fwdDependency.getVersions()
                .stream()
                .map(VersionResponse::fromVersionData)
                .collect(Collectors.toList())
        );
    }
}
