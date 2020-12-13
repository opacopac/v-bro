package com.tschanz.v_bro.app.usecase.open_repo;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class MockConnectionParametersRequest extends ConnectionParametersRequest {
    public MockConnectionParametersRequest() {
        super(RepoType.MOCK);
    }
}
