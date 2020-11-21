package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;

import com.tschanz.v_bro.common.cache.Cache;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Logger;


@RequiredArgsConstructor
public class JdbcRepoMetadataServiceCache implements JdbcRepoMetadataService {
    private final Logger logger = Logger.getLogger(JdbcRepoMetadataServiceCache.class.getName());
    private final JdbcRepoMetadataServiceImpl jdbcRepoMetadataService;
    private final Cache<RepoTable> readTableStructureCache;


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
    public String escapeUnderscore(String tableNamePattern) throws RepoException {
        return this.jdbcRepoMetadataService.escapeUnderscore(tableNamePattern);
    }
}
