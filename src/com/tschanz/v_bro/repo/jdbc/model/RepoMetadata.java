package com.tschanz.v_bro.repo.jdbc.model;

import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public interface RepoMetadata {
    RepoTable readTableStructure(String className) throws RepoException;

    String readTable(String tableName) throws RepoException;

    List<String> readPrimaryKeys(String tableName) throws RepoException;

    List<String> readIndexes(String tableName, boolean uniqueOnly) throws RepoException;

    List<RepoField> readFields(String tableName, List<String> pks, List<String> uniqueIdxes) throws RepoException;

    List<RepoRelation> readRelations(String tableName, boolean incoming) throws RepoException;

    String escapeUnderscore(String tableNamePattern) throws SQLException;

    ResultSet readTableResults(String tableNamePattern) throws SQLException;
}
