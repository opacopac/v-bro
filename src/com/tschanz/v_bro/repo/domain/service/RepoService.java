package com.tschanz.v_bro.repo.domain.service;


import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;

public interface RepoService {
    boolean isConnected();

    void connect(ConnectionParameters parameters) throws RepoException;

    void disconnect() throws RepoException;
}
