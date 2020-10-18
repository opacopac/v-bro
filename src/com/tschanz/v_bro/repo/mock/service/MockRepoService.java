package com.tschanz.v_bro.repo.mock.service;

import com.tschanz.v_bro.common.test.MockHelper;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;


public class MockRepoService implements RepoService {
    private boolean isConnected = false;
    public MockHelper<RepoException> mockHelper = new MockHelper<>();


    @Override
    public boolean isConnected() {
        this.mockHelper.reportMethodCall("isConnected");
        return this.isConnected;
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.isConnected = true;
        this.mockHelper.reportMethodCall("connect", parameters);
        this.mockHelper.checkThrowException();
    }


    @Override
    public void disconnect() throws RepoException {
        this.isConnected = false;
        this.mockHelper.reportMethodCall("disconnect");
        this.mockHelper.checkThrowException();
    }
}
