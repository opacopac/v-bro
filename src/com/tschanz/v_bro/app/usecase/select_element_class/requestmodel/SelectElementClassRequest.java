package com.tschanz.v_bro.app.usecase.select_element_class.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class SelectElementClassRequest {
    public final RepoType repoType;
    public final String elementClass;


    public SelectElementClassRequest(
        RepoType repoType,
        String elementClass
    ) {
        this.repoType = repoType;
        this.elementClass = elementClass;
    }
}
