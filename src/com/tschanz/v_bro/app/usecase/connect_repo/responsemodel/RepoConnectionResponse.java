package com.tschanz.v_bro.app.usecase.connect_repo.responsemodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public abstract class RepoConnectionResponse {
    public final RepoType repoType;


    public RepoConnectionResponse(RepoType repoType) {
        this.repoType = repoType;
    }
}
