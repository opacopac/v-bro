package com.tschanz.v_bro.app.presentation.viewmodel.repo;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.NonNull;


@Getter
public class JdbcRepoConnectionItem extends RepoConnectionItem {
    private final String url;
    private final String user;
    private final String password;
    private final String schema;


    public JdbcRepoConnectionItem(@NonNull String url, String user, String password, @NonNull String schema) {
        super(RepoType.JDBC);

        this.url = url;
        this.user = user;
        this.password = password;
        this.schema = schema;
    }
}
