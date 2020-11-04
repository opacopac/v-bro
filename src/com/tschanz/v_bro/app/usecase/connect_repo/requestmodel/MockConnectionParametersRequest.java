package com.tschanz.v_bro.app.usecase.connect_repo.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class MockConnectionParametersRequest extends ConnectionParametersRequest {
    public MockConnectionParametersRequest() {
        super(RepoType.MOCK);
    }
}
