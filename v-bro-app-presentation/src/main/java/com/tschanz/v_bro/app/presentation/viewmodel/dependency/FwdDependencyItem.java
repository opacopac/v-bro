package com.tschanz.v_bro.app.presentation.viewmodel.dependency;

import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class FwdDependencyItem {
    private final String elementClass;
    private final String elementId;
    private final List<VersionItem> versions;


    public static List<FwdDependencyItem> fromResponse(DependencyListResponse response) {
        return response.getDependencyItems()
            .stream()
            .map(FwdDependencyItem::fromResponse)
            .collect(Collectors.toList());
    }


    public static FwdDependencyItem fromResponse(DependencyResponse dependency) {
        return new FwdDependencyItem(
            dependency.getElementClass(),
            dependency.getElementId(),
            fromResponse(dependency.getVersions())
        );
    }


    public static List<VersionItem> fromResponse(List<VersionResponse> versions) {
        return versions
            .stream()
            .map(VersionItem::fromResponse)
            .collect(Collectors.toList());
    }
}
