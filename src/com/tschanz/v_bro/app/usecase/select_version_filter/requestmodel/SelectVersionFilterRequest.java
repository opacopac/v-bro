package com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class SelectVersionFilterRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final String elementId;
    public final VersionFilterRequest versionFilter;


    public SelectVersionFilterRequest(
        RepoType repoType,
        String elementClass,
        String elementId,
        VersionFilterRequest versionFilter
    ) {
        this.repoType = repoType;
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versionFilter = versionFilter;
    }
}
