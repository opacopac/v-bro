package com.tschanz.v_bro.elements.jdbc.service;

import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.model.NameFieldData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.model.Denomination;
import com.tschanz.v_bro.repo.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.jdbc.model.RowInfo;
import com.tschanz.v_bro.repo.jdbc.repo_data.JdbcRepoData;
import com.tschanz.v_bro.repo.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class JdbcElementService implements ElementService {
    public static String ELEMENT_TABLE_SUFFIX2 = "_E";

    private final Logger logger = Logger.getLogger(JdbcVersioningStructure.class.getName());
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoData repoData;


    public JdbcElementService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData,
        JdbcRepoData repoData
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
        this.repoData = repoData;
    }


    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        this.logger.info("finding tables with suffix " + ELEMENT_TABLE_SUFFIX2);
        String tableNamePattern = JdbcRepoMetadataService.WILDCARD + this.repoMetaData.escapeUnderscore(ELEMENT_TABLE_SUFFIX2);
        List<String> tableNames = this.repoMetaData.findTableNames(tableNamePattern);

        return tableNames
            .stream()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }


    @Override
    public List<Denomination> readDenominations(String elementName) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        RepoTable repoTable = this.repoMetaData.readTableStructure(elementName);

        return repoTable.getFields()
            .stream()
            .map(dbField -> new Denomination(dbField.getName()))
            .collect(Collectors.toList());
    }


    @Override
    public Collection<ElementData> readElements(String elementName, Collection<String> fieldNames) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        RepoTable repoTable = this.repoMetaData.readTableStructure(elementName);
        List<RepoField> repoFields = repoTable.getFields()
            .stream()
            .filter(field -> fieldNames.contains(field.getName()))
            .collect(Collectors.toList());

        String idFieldName = repoTable.findIdFields()
            .stream()
            .map(RepoField::getName)
            .findFirst()
            .orElse(null);

        List<RowInfo> elementRows = this.repoData.readData(elementName, repoFields, Collections.emptyList());
        List<ElementData> elementList = elementRows
            .stream()
            .map(row -> {
                String id = row.getFieldValueList().get(idFieldName).getValueString();
                List<NameFieldData> nameFieldDataList = fieldNames
                    .stream()
                    .map(fieldName -> new NameFieldData(fieldName, row.getFieldValueList().get(fieldName).getValueString()))
                    .collect(Collectors.toList());

                return new ElementData(id, nameFieldDataList);
            })
            .collect(Collectors.toList());

        return elementList; // TODO;
    }
}
