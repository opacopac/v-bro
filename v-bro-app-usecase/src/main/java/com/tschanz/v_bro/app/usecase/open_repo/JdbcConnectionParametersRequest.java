package com.tschanz.v_bro.app.usecase.open_repo;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class JdbcConnectionParametersRequest extends ConnectionParametersRequest {
    public final String url;
    public final String user;
    public final String password;


    public JdbcConnectionParametersRequest(String url, String user, String password) {
        super(RepoType.JDBC);
        this.url = url;
        this.user = user;
        this.password = password;
    }
}
