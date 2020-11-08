package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;

import java.util.List;


public interface JdbcRepoMetadataService {
    List<String> findTableNames(String tableNamePattern) throws RepoException;

    RepoTable readTableStructure(String className) throws RepoException;

    String escapeUnderscore(String tableNamePattern) throws RepoException;
}
