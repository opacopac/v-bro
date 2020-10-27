package com.tschanz.v_bro.repo.persistence.mock.service;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;


public class MockRepoService2 implements RepoService {
    private boolean isConnected = false;


    @Override
    public boolean isConnected() {
        return this.isConnected;
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.isConnected = true;
    }


    @Override
    public void disconnect() throws RepoException {
        this.isConnected = false;
    }
}
