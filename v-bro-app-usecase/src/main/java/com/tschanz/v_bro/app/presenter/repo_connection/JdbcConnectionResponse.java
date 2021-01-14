package com.tschanz.v_bro.app.presenter.repo_connection;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class JdbcConnectionResponse extends RepoConnectionResponse {
    public final String url;
    public final String user;
    public final String password;
    public final String schema;


    public JdbcConnectionResponse(String url, String user, String password, String schema) {
        super(RepoType.JDBC);
        this.url = url;
        this.user = user;
        this.password = password;
        this.schema = schema;
    };
}
