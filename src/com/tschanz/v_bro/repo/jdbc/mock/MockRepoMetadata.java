package com.tschanz.v_bro.repo.jdbc.mock;

import com.tschanz.v_bro.common.test.MockHelper;
import com.tschanz.v_bro.common.test.MockReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.jdbc.model.RepoMetadata;
import com.tschanz.v_bro.repo.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.jdbc.model.RepoRelation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class MockRepoMetadata implements RepoMetadata {
    public MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<RepoTable> readTableStructureResults = new MockReturnValue<>("readTableStructureResults");
    public MockReturnValue<String> readTableResults = new MockReturnValue<>("readTableResults");
    public MockReturnValue<List<String>> readPrimaryKeysResults = new MockReturnValue<>("readPrimaryKeysResults");
    public MockReturnValue<List<String>> readIndexesResults = new MockReturnValue<>("readIndexesResults");
    public MockReturnValue<List<RepoField>> readFieldsResults = new MockReturnValue<>("readFieldsResults");
    public MockReturnValue<List<RepoRelation>> readRelationsResults = new MockReturnValue<>("readRelationsResults");
    public MockReturnValue<String> escapeUnderscoreResults = new MockReturnValue<>("escapeUnderscoreResults");
    public MockReturnValue<ResultSet> readTableResultsResults = new MockReturnValue<>("readTableResultsResults");


    @Override
    public RepoTable readTableStructure(String className) throws RepoException {
        this.mockHelper.reportMethodCall("readTableStructure", className);
        this.mockHelper.checkThrowException();

        return this.readTableStructureResults.next();
    }


    @Override
    public String readTable(String tableName) throws RepoException {
        this.mockHelper.reportMethodCall("readTable", tableName);
        this.mockHelper.checkThrowException();

        return this.readTableResults.next();
    }


    @Override
    public List<String> readPrimaryKeys(String tableName) throws RepoException {
        this.mockHelper.reportMethodCall("readPrimaryKeys", tableName);
        this.mockHelper.checkThrowException();

        return this.readPrimaryKeysResults.next();
    }


    @Override
    public List<String> readIndexes(String tableName, boolean uniqueOnly) throws RepoException {
        this.mockHelper.reportMethodCall("readIndexes", tableName, uniqueOnly);
        this.mockHelper.checkThrowException();

        return this.readIndexesResults.next();
    }


    @Override
    public List<RepoField> readFields(String tableName, List<String> pks, List<String> uniqueIdxes) throws RepoException {
        this.mockHelper.reportMethodCall("readFields", tableName, pks, uniqueIdxes);
        this.mockHelper.checkThrowException();

        return this.readFieldsResults.next();
    }


    @Override
    public List<RepoRelation> readRelations(String tableName, boolean incoming) throws RepoException {
        this.mockHelper.reportMethodCall("readRelations", tableName, incoming);
        this.mockHelper.checkThrowException();

        return this.readRelationsResults.next();
    }


    @Override
    public String escapeUnderscore(String tableNamePattern) throws SQLException {
        this.mockHelper.reportMethodCall("escapeUnderscore", tableNamePattern);

        return this.escapeUnderscoreResults.next();
    }


    @Override
    public ResultSet readTableResults(String tableNamePattern) throws SQLException {
        this.mockHelper.reportMethodCall("readTableResults", tableNamePattern);

        return this.readTableResultsResults.next();
    }
}
