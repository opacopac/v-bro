package com.tschanz.v_bro.app.presenter.dependencies;

import com.tschanz.v_bro.data_structure.domain.model.Dependency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class DependencyListResponse {
    private final List<DependencyResponse> dependencyItems;


    public static DependencyListResponse fromDomain(List<Dependency> fwdDependencies) {
        return new DependencyListResponse(fwdDependencies
            .stream()
            .map(DependencyResponse::fromDependency)
            .collect(Collectors.toList())
        );
    }
}
