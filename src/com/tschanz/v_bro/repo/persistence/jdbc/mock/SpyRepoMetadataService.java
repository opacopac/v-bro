package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;

import java.util.List;


public class SpyRepoMetadataService implements JdbcRepoMetadataService {
    public SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<RepoTable> readTableStructureResults = new SpyReturnValue<>("readTableStructureResults");
    public SpyReturnValue<String> readTableResults = new SpyReturnValue<>("readTableResults");
    public SpyReturnValue<List<String>> readPrimaryKeysResults = new SpyReturnValue<>("readPrimaryKeysResults");
    public SpyReturnValue<List<String>> readIndexesResults = new SpyReturnValue<>("readIndexesResults");
    public SpyReturnValue<List<RepoField>> readFieldsResults = new SpyReturnValue<>("readFieldsResults");
    public SpyReturnValue<List<RepoRelation>> readRelationsResults = new SpyReturnValue<>("readRelationsResults");
    public SpyReturnValue<String> escapeUnderscoreResults = new SpyReturnValue<>("escapeUnderscoreResults");
    public SpyReturnValue<List<String>> readTableResultsResults = new SpyReturnValue<>("readTableResultsResults");


    @Override
    public RepoTable readTableStructure(String className) throws RepoException {
        this.spyHelper.reportMethodCall("readTableStructure", className);
        this.spyHelper.checkThrowException();

        return this.readTableStructureResults.next();
    }


    @Override
    public String readTable(String tableName) throws RepoException {
        this.spyHelper.reportMethodCall("readTable", tableName);
        this.spyHelper.checkThrowException();

        return this.readTableResults.next();
    }


    @Override
    public List<String> readPrimaryKeys(String tableName) throws RepoException {
        this.spyHelper.reportMethodCall("readPrimaryKeys", tableName);
        this.spyHelper.checkThrowException();

        return this.readPrimaryKeysResults.next();
    }


    @Override
    public List<String> readIndexes(String tableName, boolean uniqueOnly) throws RepoException {
        this.spyHelper.reportMethodCall("readIndexes", tableName, uniqueOnly);
        this.spyHelper.checkThrowException();

        return this.readIndexesResults.next();
    }


    @Override
    public List<RepoField> readFields(String tableName, List<String> pks, List<String> uniqueIdxes) throws RepoException {
        this.spyHelper.reportMethodCall("readFields", tableName, pks, uniqueIdxes);
        this.spyHelper.checkThrowException();

        return this.readFieldsResults.next();
    }


    @Override
    public List<RepoRelation> readRelations(String tableName, boolean incoming) throws RepoException {
        this.spyHelper.reportMethodCall("readRelations", tableName, incoming);
        this.spyHelper.checkThrowException();

        return this.readRelationsResults.next();
    }


    @Override
    public String escapeUnderscore(String tableNamePattern) throws RepoException {
        this.spyHelper.reportMethodCall("escapeUnderscore", tableNamePattern);

        return this.escapeUnderscoreResults.next();
    }


    @Override
    public List<String> findTableNames(String tableNamePattern) throws RepoException {
        this.spyHelper.reportMethodCall("readTableResults", tableNamePattern);

        return this.readTableResultsResults.next();
    }
}
