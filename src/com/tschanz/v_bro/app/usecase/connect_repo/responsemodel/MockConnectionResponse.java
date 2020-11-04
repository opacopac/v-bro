package com.tschanz.v_bro.app.usecase.connect_repo.responsemodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class MockConnectionResponse extends RepoConnectionResponse {
    public MockConnectionResponse() {
        super(RepoType.MOCK);
    }
}
