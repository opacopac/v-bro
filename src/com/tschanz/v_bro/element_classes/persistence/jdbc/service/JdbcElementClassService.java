package com.tschanz.v_bro.element_classes.persistence.jdbc.service;

import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class JdbcElementClassService implements ElementClassService {
    public static String ELEMENT_TABLE_SUFFIX = "_E";

    private final Logger logger = Logger.getLogger(JdbcElementClassService.class.getName());
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;


    public JdbcElementClassService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
    }


    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        this.logger.info("finding tables with suffix " + ELEMENT_TABLE_SUFFIX);
        String tableNamePattern = JdbcRepoMetadataServiceImpl.WILDCARD + this.repoMetaData.escapeUnderscore(ELEMENT_TABLE_SUFFIX);
        List<String> tableNames = this.repoMetaData.findTableNames(tableNamePattern);

        return tableNames
            .stream()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        RepoTable repoTable = this.repoMetaData.readTableStructure(elementClass);

        return repoTable.getFields()
            .stream()
            .map(dbField -> new Denomination(dbField.getName()))
            .collect(Collectors.toList());
    }
}
