package com.tschanz.v_bro.app.usecase.connect_repo.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public abstract class ConnectionParametersRequest {
    public final RepoType repoType;


    public ConnectionParametersRequest(RepoType repoType) {
        this.repoType = repoType;
    }
}
