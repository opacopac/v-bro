package com.tschanz.v_bro.repo.presentation.swing.viewmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class JdbcRepoConnectionItem extends RepoConnectionItem {
    private final String url;
    private final String user;
    private final String password;


    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }


    public JdbcRepoConnectionItem(String url, String user, String password) {
        super(RepoType.JDBC);

        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }

        this.url = url;
        this.user = user;
        this.password = password;
    }
}
