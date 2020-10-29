package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;

import java.util.List;


public interface JdbcRepoMetadataService {
    List<String> findTableNames(String tableNamePattern) throws RepoException;

    RepoTable readTableStructure(String className) throws RepoException;

    String readTable(String tableName) throws RepoException;

    List<String> readPrimaryKeys(String tableName) throws RepoException;

    List<String> readIndexes(String tableName, boolean uniqueOnly) throws RepoException;

    List<RepoField> readFields(String tableName, List<String> pks, List<String> uniqueIdxes) throws RepoException;

    List<RepoRelation> readRelations(String tableName, boolean incoming) throws RepoException;

    String escapeUnderscore(String tableNamePattern) throws RepoException;
}
