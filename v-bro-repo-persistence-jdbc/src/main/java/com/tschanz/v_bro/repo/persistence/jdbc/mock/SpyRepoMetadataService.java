package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;


public class SpyRepoMetadataService implements JdbcRepoMetadataService {
    public SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<RepoTable> readTableStructureResults = new SpyReturnValue<>("readTableStructureResults");


    @Override
    public RepoTable readTableStructure(String className) throws RepoException {
        this.spyHelper.reportMethodCall("readTableStructure", className);
        this.spyHelper.checkThrowException();

        return this.readTableStructureResults.next();
    }
}
