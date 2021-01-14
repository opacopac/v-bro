package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;


public interface JdbcRepoMetadataService {
    RepoTable readTableStructure(String className) throws RepoException;
}
