package com.tschanz.v_bro.app.presentation.viewmodel.repo;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QuickConnectionItem {
    private final String key;
    private final RepoType repoType;
    private final String url;
    private final String user;
    private final String password;
    private final String filename;


    public static QuickConnectionItem createEmptyItem(String key) {
        return new QuickConnectionItem(key, null, null, null, null, null);
    }


    @Override
    public String toString() {
        return this.key;
    }
}
