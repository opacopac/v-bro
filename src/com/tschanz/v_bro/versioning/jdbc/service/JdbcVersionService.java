package com.tschanz.v_bro.versioning.jdbc.service;

import com.tschanz.v_bro.repo.jdbc.repo_data.JdbcRepoData;
import com.tschanz.v_bro.repo.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versioning.domain.model.VersionAggregate;
import com.tschanz.v_bro.versioning.domain.model.VersionInfo;
import com.tschanz.v_bro.versioning.domain.service.VersionService;

import java.util.Collection;


public class JdbcVersionService implements VersionService {
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoData repoData;


    public JdbcVersionService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData,
        JdbcRepoData repoData
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
        this.repoData = repoData;
    }


    @Override
    public Collection<VersionInfo> readVersionTimeline(String elementName, String elementId) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }


        return null;
    }


    @Override
    public VersionAggregate readVersionAggregate(String elementName, String elementId, String versionId) throws RepoException {
        return null;
    }
}
