package com.tschanz.v_bro.app.usecase.select_element.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;


public class SelectElementRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final String elementId;
    public final VersionFilterRequest versionFilter;


    public SelectElementRequest(
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
