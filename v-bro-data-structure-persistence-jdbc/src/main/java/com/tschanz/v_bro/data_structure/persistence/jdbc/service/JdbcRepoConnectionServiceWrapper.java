package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoConnectionService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoConnectionService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JdbcRepoConnectionServiceWrapper implements RepoConnectionService {
    private final JdbcRepoConnectionService jdbcRepoConnectionService;
    private final JdbcDataStructureService jdbcDataStructureService;


    @Override
    public boolean isConnected() {
        return this.jdbcRepoConnectionService.isConnected();
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.jdbcRepoConnectionService.connect(parameters);
        this.jdbcDataStructureService.readAggregateStructures();
    }


    @Override
    public void disconnect() throws RepoException {
        this.jdbcRepoConnectionService.disconnect();
    }
}
