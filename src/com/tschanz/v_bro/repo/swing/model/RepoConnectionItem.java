package com.tschanz.v_bro.repo.swing.model;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public abstract class RepoConnectionItem {
    public final RepoType repoType;


    public RepoType getRepoType() { return repoType; }


    public RepoConnectionItem(RepoType repoType) {
        if (repoType == null) {
            throw new IllegalArgumentException("repoType must not be null");
        }

        this.repoType = repoType;
    }
}
