package com.tschanz.v_bro.app.usecase.select_dependency_version.requestmodel;

import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;
import com.tschanz.v_bro.repo.domain.model.RepoType;


public class SelectDependencyVersionRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final String elementId;
    public final String versionId;
    public final VersionFilterRequest versionFilter;
    public final DependencyFilterRequest dependencyFilter;


    public SelectDependencyVersionRequest(
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
