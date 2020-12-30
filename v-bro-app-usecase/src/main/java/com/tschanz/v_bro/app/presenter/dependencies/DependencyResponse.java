package com.tschanz.v_bro.app.presenter.dependencies;

import com.tschanz.v_bro.app.presenter.version_timeline.VersionResponse;
import com.tschanz.v_bro.data_structure.domain.model.Dependency;
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


    public static DependencyResponse fromDependency(Dependency dependency) {
        return new DependencyResponse(
            dependency.getElementClass().getName(),
            dependency.getElement().getId(),
            dependency.getVersions()
                .stream()
                .map(VersionResponse::fromVersionData)
                .collect(Collectors.toList())
        );
    }
}
