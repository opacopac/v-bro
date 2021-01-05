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
public class DependencyItem {
    private final String elementClass;
    private final String elementId;
    private final List<VersionItem> versions;
    private final List<String> denominations;


    public static List<DependencyItem> fromResponse(DependencyListResponse response) {
        return response.getDependencyItems()
            .stream()
            .map(DependencyItem::fromResponse)
            .collect(Collectors.toList());
    }


    public static DependencyItem fromResponse(DependencyResponse dependency) {
        return new DependencyItem(
            dependency.getElementClass(),
            dependency.getElementId(),
            fromResponse(dependency.getVersions()),
            dependency.getDenominations()
        );
    }


    public static List<VersionItem> fromResponse(List<VersionResponse> versions) {
        return versions
            .stream()
            .map(VersionItem::fromResponse)
            .collect(Collectors.toList());
    }
}
