package com.tschanz.v_bro.repo.jdbc.model;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;


public class JdbcConnectionParameters implements ConnectionParameters {
    private final String url;
    private final String user;
    private final String password;

    @Override
    public RepoType getType() { return RepoType.JDBC; }
    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }


    public JdbcConnectionParameters(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
}
