package com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;


public class SelectDependencyFilterRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final String elementId;
    public final String versionId;
    public final VersionFilterRequest versionFilter;
    public final DependencyFilterRequest dependencyFilter;


    public SelectDependencyFilterRequest(
        RepoType repoType,
        String elementClass,
        String elementId,
        String versionId,
        VersionFilterRequest versionFilter,
        DependencyFilterRequest dependencyFilter
    ) {
        this.repoType = repoType;
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versionId = versionId;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
    }
}
