package com.tschanz.v_bro.repo.persistence.mock.model;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;


public class MockConnectionParameters implements ConnectionParameters {
    @Override
    public RepoType getType() {
        return RepoType.MOCK;
    }
}
