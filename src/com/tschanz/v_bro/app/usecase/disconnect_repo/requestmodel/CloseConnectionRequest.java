package com.tschanz.v_bro.app.usecase.disconnect_repo.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class CloseConnectionRequest {
    public final RepoType repoType;


    public CloseConnectionRequest(RepoType repoType) {
        this.repoType = repoType;
    }
}
