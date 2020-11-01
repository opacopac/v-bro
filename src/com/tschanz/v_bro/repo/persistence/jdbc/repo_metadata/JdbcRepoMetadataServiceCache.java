package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;

import com.tschanz.v_bro.common.cache.Cache;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;

import java.util.List;
import java.util.logging.Logger;


public class JdbcRepoMetadataServiceCache implements JdbcRepoMetadataService {
    private final Logger logger = Logger.getLogger(JdbcRepoMetadataServiceCache.class.getName());
    private final JdbcRepoMetadataServiceImpl jdbcRepoMetadataService;
    private final Cache<RepoTable> readTableStructureCache;


    public JdbcRepoMetadataServiceCache(
        JdbcRepoMetadataServiceImpl jdbcRepoMetadataService,
        Cache<RepoTable> readTableStructureCache
    ) {
        this.jdbcRepoMetadataService = jdbcRepoMetadataService;
        this.readTableStructureCache = readTableStructureCache;
    }


    @Override
    public List<String> findTableNames(String tableNamePattern) throws RepoException {
        return this.jdbcRepoMetadataService.findTableNames(tableNamePattern);
    }


    @Override
    public RepoTable readTableStructure(String className) throws RepoException {
        RepoTable cachedResult = this.readTableStructureCache.getItem(className);
        if (cachedResult != null) {
            this.logger.info("serving from cache: structure for table '" + className + "'");
            return cachedResult;
        } else {
            RepoTable result = this.jdbcRepoMetadataService.readTableStructure(className);
            this.readTableStructureCache.addItem(className, result);
            return result;
        }
    }


    @Override
    public String readTable(String tableName) throws RepoException {
        return this.jdbcRepoMetadataService.readTable(tableName);
    }


    @Override
    public List<String> readPrimaryKeys(String tableName) throws RepoException {
        return this.jdbcRepoMetadataService.readPrimaryKeys(tableName);
    }


    @Override
    public List<String> readIndexes(String tableName, boolean uniqueOnly) throws RepoException {
        return this.jdbcRepoMetadataService.readIndexes(tableName, uniqueOnly);
    }


    @Override
    public List<RepoField> readFields(String tableName, List<String> pks, List<String> uniqueIdxes) throws RepoException {
        return this.jdbcRepoMetadataService.readFields(tableName, pks, uniqueIdxes);
    }


    @Override
    public List<RepoRelation> readRelations(String tableName, boolean incoming) throws RepoException {
        return this.jdbcRepoMetadataService.readRelations(tableName, incoming);
    }


    @Override
    public String escapeUnderscore(String tableNamePattern) throws RepoException {
        return this.jdbcRepoMetadataService.escapeUnderscore(tableNamePattern);
    }
}
