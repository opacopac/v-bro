package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class JdbcElementClassService implements ElementClassService {
    private final static String ELEMENT_TABLE_SUFFIX = "_E";
    private final static List<String> NO_DENOMINATION_NAME = List.of("VERSIONID", "CREATED_BY", "MODIFIED_BY", "BEMERKUNG"); // TODO => app config
    private final static List<RepoFieldType> NO_DENOMINATION_TYPE = List.of(RepoFieldType.BOOL, RepoFieldType.DATE, RepoFieldType.TIMESTAMP);
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;


    @Override
    public List<ElementClass> readElementClasses() throws RepoException {
        log.info("finding tables with suffix " + ELEMENT_TABLE_SUFFIX);
        String tableNamePattern = JdbcRepoMetadataServiceImpl.WILDCARD + this.repoMetaData.escapeUnderscore(ELEMENT_TABLE_SUFFIX);
        List<String> tableNames = this.repoMetaData.findTableNames(tableNamePattern);

        return tableNames
            .stream()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        RepoTable repoTable = this.repoMetaData.readTableStructure(elementClass);

        return repoTable.getFields()
            .stream()
            .filter(dbField -> !dbField.isId())
            .filter(dbField -> !NO_DENOMINATION_NAME.contains(dbField.getName()))
            .filter(dbField -> !NO_DENOMINATION_TYPE.contains(dbField.getType()))
            //.sorted(new DenominationFieldComparator())
            .map(dbField -> new Denomination(dbField.getName()))
            .collect(Collectors.toList());
    }
}
