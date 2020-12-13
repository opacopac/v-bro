package com.tschanz.v_bro.app.presenter.repo;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class RepoResponse {
    private final RepoConnectionResponse connection;


    public static RepoResponse fromDomain(ConnectionParameters connectionParameters) {
        if (connectionParameters == null) {
            return new RepoResponse(null);
        } else {
            return new RepoResponse(RepoConnectionResponse.fromDomain(connectionParameters));
        }
    }
}
