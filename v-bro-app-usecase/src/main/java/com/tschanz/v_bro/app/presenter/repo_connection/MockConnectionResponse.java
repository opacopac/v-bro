package com.tschanz.v_bro.app.presenter.repo_connection;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class MockConnectionResponse extends RepoConnectionResponse {
    public MockConnectionResponse() {
        super(RepoType.MOCK);
    }
}
