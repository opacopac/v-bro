package com.tschanz.v_bro.repo.presentation.swing.viewmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class QuickConnectionItem {
    private final String key;
    private final RepoType repoType;
    private final String url;
    private final String user;
    private final String password;
    private final String filename;


    public String getKey() { return key; }
    public RepoType getRepoType() { return repoType; }
    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
    public String getFilename() { return filename; }


    public QuickConnectionItem(
        String key,
        RepoType repoType,
        String url,
        String user,
        String password,
        String filename
    ) {
        this.key = key;
        this.repoType = repoType;
        this.url = url;
        this.user = user;
        this.password = password;
        this.filename = filename;
    }


    public static QuickConnectionItem createEmptyItem(String key) {
        return new QuickConnectionItem(key, null, null, null, null, null);
    }


    @Override
    public String toString() {
        return this.key;
    }
}
