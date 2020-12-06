package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcElementService implements ElementService {
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoDataService repoData;


    @Override
    public List<ElementData> readElements(@NonNull String elementClass, @NonNull Collection<String> fieldNames, @NonNull String query, int maxResults) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        var repoTable = this.readElementTable(elementClass);
        var repoFields = this.getRepoFields(repoTable, fieldNames);
        var orFilter = this.getOrFilter(repoTable, repoFields, query);

        var elementRows = this.repoData.readRepoTableRecords(repoTable, repoFields, Collections.emptyList(), orFilter, maxResults);
        var elementList = elementRows
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
            .filter(field -> fieldNames.contains(field.getName()) || field.isId())
            .collect(Collectors.toList());
    }


    private List<RowFilter> getOrFilter(RepoTable repoTable, List<RepoField> repoFields, String query) {
        if (query.isEmpty()) {
            return Collections.emptyList();
        } else {
            return repoFields
                .stream()
                .map(f -> new RowFilter(f, RowFilterOperator.LIKE, JdbcRepoMetadataServiceImpl.WILDCARD + query + JdbcRepoMetadataServiceImpl.WILDCARD))
                .collect(Collectors.toList());
        }
    }
}
