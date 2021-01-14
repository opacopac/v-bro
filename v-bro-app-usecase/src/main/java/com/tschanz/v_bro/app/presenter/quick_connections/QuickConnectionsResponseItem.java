package com.tschanz.v_bro.app.presenter.quick_connections;

import com.tschanz.v_bro.repo.domain.model.QuickConnection;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QuickConnectionsResponseItem {
    private final String key;
    private final RepoType repoType;
    private final String url;
    private final String user;
    private final String password;
    private final String schema;
    private final String filename;


    public static QuickConnectionsResponseItem fromDomain(QuickConnection quickConnection) {
        return new QuickConnectionsResponseItem(
            quickConnection.getKey(),
            quickConnection.getRepoType(),
            quickConnection.getUrl(),
            quickConnection.getUser(),
            quickConnection.getPassword(),
            quickConnection.getSchema(),
            quickConnection.getFilename()
        );
    }
}
