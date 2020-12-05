package com.tschanz.v_bro.app.usecase.connect_repo.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class ConnectionParametersRequest {
    public final RepoType repoType;
}
