package com.tschanz.v_bro.versioning.jdbc.service;

import com.tschanz.v_bro.repo.jdbc.service.JdbcRepoService;
import com.tschanz.v_bro.repo.jdbc.service.JdbcRepoMetadata;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versioning.domain.model.VersionData;
import com.tschanz.v_bro.versioning.domain.service.VersionService;

import java.util.Collection;


public class JdbcVersionService implements VersionService {
    private final JdbcRepoService repo;
    private final JdbcRepoMetadata repoMetaData;


    public JdbcVersionService(
        JdbcRepoService repo,
        JdbcRepoMetadata repoMetaData
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
    }


    @Override
    public Collection<VersionData> readVersions(String elementName, String elementId) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }


        return null;
    }
}
