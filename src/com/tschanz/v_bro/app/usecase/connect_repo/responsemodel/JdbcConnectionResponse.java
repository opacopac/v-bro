package com.tschanz.v_bro.app.usecase.connect_repo.responsemodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class JdbcConnectionResponse extends RepoConnectionResponse {
    public final String url;
    public final String user;
    public final String password;


    public JdbcConnectionResponse(String url, String user, String password) {
        super(RepoType.JDBC);
        this.url = url;
        this.user = user;
        this.password = password;
    };
}
