package com.tschanz.v_bro.repo.persistence.jdbc.model;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JdbcConnectionParameters implements ConnectionParameters {
    @Getter private final String url;
    @Getter private final String user;
    @Getter private final String password;


    @Override
    public RepoType getRepoType() { return RepoType.JDBC; }
}
