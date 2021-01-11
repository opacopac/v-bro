package com.tschanz.v_bro.repo.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QuickConnection {
    private final String key;
    private final RepoType repoType;
    private final String url;
    private final String user;
    private final String password;
    private final String filename;


    public static QuickConnection createEmptyItem(String key) {
        return new QuickConnection(key, null, null, null, null, null);
    }
}
