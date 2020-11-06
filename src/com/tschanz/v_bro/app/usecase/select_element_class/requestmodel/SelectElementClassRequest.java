package com.tschanz.v_bro.app.usecase.select_element_class.requestmodel;

import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;
import com.tschanz.v_bro.repo.domain.model.RepoType;


public class SelectElementClassRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final VersionFilterRequest versionFilter;
    public final DependencyFilterRequest dependencyFilter;


    public SelectElementClassRequest(
        RepoType repoType,
        String elementClass,
        VersionFilterRequest versionFilter,
        DependencyFilterRequest dependencyFilter
    ) {
        this.repoType = repoType;
        this.elementClass = elementClass;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
    }
}
