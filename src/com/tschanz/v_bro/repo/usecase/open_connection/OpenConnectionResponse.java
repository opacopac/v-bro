package com.tschanz.v_bro.repo.usecase.open_connection;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class OpenConnectionResponse {
    public final RepoConnection repoConnection;

    public OpenConnectionResponse(RepoConnection repoConnection) {
        this.repoConnection = repoConnection;
    }


    public static class RepoConnection {
        public final RepoType repoType;


        public RepoConnection(RepoType repoType) {
            this.repoType = repoType;
        }
    }
}
