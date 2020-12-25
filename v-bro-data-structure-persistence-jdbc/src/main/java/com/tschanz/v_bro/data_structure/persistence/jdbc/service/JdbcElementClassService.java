package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class JdbcElementClassService implements ElementClassService {
    private final JdbcRepoMetadataService repoMetadataService;


    @Override
    public List<ElementClass> readAllElementClasses() throws RepoException {
        log.info("finding tables with suffix " + ElementTable.TABLE_SUFFIX);
        String tableNamePattern = JdbcRepoMetadataServiceImpl.WILDCARD + this.repoMetadataService.escapeUnderscore(ElementTable.TABLE_SUFFIX);
        List<String> tableNames = this.repoMetadataService.findTableNames(tableNamePattern);

        return tableNames
            .stream()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }
}
