package com.tschanz.v_bro.app.usecase.select_version.requestmodel;

import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.repo.domain.model.RepoType;


public class SelectVersionRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final String elementId;
    public final String versionId;
    public final VersionFilterRequest versionFilter;
    public final DependencyFilterRequest dependencyFilter;


    public SelectVersionRequest(
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
