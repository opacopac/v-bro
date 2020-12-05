package com.tschanz.v_bro.app.usecase.connect_repo.responsemodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class RepoConnectionResponse {
    public final RepoType repoType;
}
