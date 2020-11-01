package com.tschanz.v_bro.elements.persistence.jdbc.service;

import com.tschanz.v_bro.elements.domain.model.DenominationData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class JdbcElementService implements ElementService {
    private final Logger logger = Logger.getLogger(JdbcElementService.class.getName());
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoDataService repoData;


    public JdbcElementService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData,
        JdbcRepoDataService repoData
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
        this.repoData = repoData;
    }


    @Override
    public Collection<ElementData> readElements(String elementClass, Collection<String> fieldNames) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        RepoTable repoTable = this.readElementTable(elementClass);
        List<RepoField> repoFields = this.getRepoFields(repoTable, fieldNames);
        String idFieldName = repoTable.findfirstIdField().getName();

        List<RepoTableRecord> elementRows = this.repoData.readRepoTableRecords(repoTable, repoFields, Collections.emptyList());
        List<ElementData> elementList = elementRows
            .stream()
            .map(row -> {
                String id = row.findIdFieldValue().getValueString();
                List<DenominationData> denominationDataList = fieldNames
                    .stream()
                    .map(fieldName -> new DenominationData(fieldName, row.findFieldValue(fieldName).getValueString()))
                    .collect(Collectors.toList());

                return new ElementData(id, denominationDataList);
            })
            .collect(Collectors.toList());

        return elementList;
    }


    public RepoTable readElementTable(String elementClass) throws RepoException {
        return this.repoMetaData.readTableStructure(elementClass);
    }


    private List<RepoField> getRepoFields(RepoTable repoTable, Collection<String> fieldNames) {
        return repoTable.getFields()
            .stream()
            .filter(field -> fieldNames.contains(field.getName()) || field.getIsId())
            .collect(Collectors.toList());
    }
}
