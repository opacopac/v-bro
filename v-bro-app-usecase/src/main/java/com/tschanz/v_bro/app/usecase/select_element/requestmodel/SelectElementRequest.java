package com.tschanz.v_bro.app.usecase.select_element.requestmodel;

import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;


public class SelectElementRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final String elementId;
    public final VersionFilterRequest versionFilter;
    public final DependencyFilterRequest dependencyFilter;

    public SelectElementRequest(
        RepoType repoType,
        String elementClass,
        String elementId,
        VersionFilterRequest versionFilter,
        DependencyFilterRequest dependencyFilter
    ) {
        this.repoType = repoType;
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
    }
}
