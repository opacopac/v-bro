package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;

import java.util.List;


public class SpyRepoMetadataService implements JdbcRepoMetadataService {
    public SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<String>> findTableNamesResults = new SpyReturnValue<>("findTableNamesResults");
    public SpyReturnValue<RepoTable> readTableStructureResults = new SpyReturnValue<>("readTableStructureResults");
    public SpyReturnValue<String> escapeUnderscoreResults = new SpyReturnValue<>("escapeUnderscoreResults");


    @Override
    public List<String> findTableNames(String tableNamePattern) throws RepoException {
        this.spyHelper.reportMethodCall("findTableNames", tableNamePattern);

        return this.findTableNamesResults.next();
    }


    @Override
    public RepoTable readTableStructure(String className) throws RepoException {
        this.spyHelper.reportMethodCall("readTableStructure", className);
        this.spyHelper.checkThrowException();

        return this.readTableStructureResults.next();
    }


    @Override
    public String escapeUnderscore(String tableNamePattern) throws RepoException {
        this.spyHelper.reportMethodCall("escapeUnderscore", tableNamePattern);

        return this.escapeUnderscoreResults.next();
    }
}
