package com.tschanz.v_bro.repo.domain.service;


import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;

public interface RepoConnectionService {
    boolean isConnected();

    void connect(ConnectionParameters parameters) throws RepoException;

    void disconnect() throws RepoException;
}
