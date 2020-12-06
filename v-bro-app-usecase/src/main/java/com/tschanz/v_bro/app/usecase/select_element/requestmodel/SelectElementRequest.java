package com.tschanz.v_bro.app.usecase.select_element.requestmodel;

import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SelectElementRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final String elementId;
    public final VersionFilterRequest versionFilter;
    public final DependencyFilterRequest dependencyFilter;
}
