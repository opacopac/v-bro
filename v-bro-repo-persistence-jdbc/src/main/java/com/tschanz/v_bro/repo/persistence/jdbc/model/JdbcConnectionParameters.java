package com.tschanz.v_bro.repo.persistence.jdbc.model;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class JdbcConnectionParameters implements ConnectionParameters {
    @NonNull private final String url;
    private final String user;
    private final String password;
    @NonNull private final String schema;


    @Override
    public RepoType getRepoType() {
        return RepoType.JDBC;
    }
}
