package com.tschanz.v_bro.repo.persistence;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;


public class SpyRepoService implements RepoService {
    private boolean isConnected = false;
    public SpyHelper<RepoException> spyHelper = new SpyHelper<>();


    @Override
    public boolean isConnected() {
        this.spyHelper.reportMethodCall("isConnected");
        return this.isConnected;
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.isConnected = true;
        this.spyHelper.reportMethodCall("connect", parameters);
        this.spyHelper.checkThrowException();
    }


    @Override
    public void disconnect() throws RepoException {
        this.isConnected = false;
        this.spyHelper.reportMethodCall("disconnect");
        this.spyHelper.checkThrowException();
    }
}
