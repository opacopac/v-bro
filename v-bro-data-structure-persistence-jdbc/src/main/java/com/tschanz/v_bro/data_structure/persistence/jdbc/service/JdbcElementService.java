package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementRecord;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementTable;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.*;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class JdbcElementService implements ElementService {
    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoDataService repoData;


    @Override
    public List<ElementData> queryElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    ) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        var elementTable = this.readElementTable(elementClass.getName());
        var versionTable = this.readVersionTable(elementTable);
        var allFields = this.getFields(elementTable, versionTable, denominationFields);
        var optFilter = this.getQueryFilter(allFields, query);
        var joins = this.getTableJoins(elementTable, versionTable);
        var rows = this.repoData.readRepoTableRecords(elementTable.getRepoTable(), joins, allFields, Collections.emptyList(), optFilter, maxResults);

        return rows
            .stream()
            .map(row -> this.getElementFromRow(row, elementClass))
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public ElementData readElement(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String elementId
    ) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        var elementTable = this.readElementTable(elementClass.getName());
        var versionTable = this.readVersionTable(elementTable);
        var allFields = this.getFields(elementTable, versionTable, denominationFields);
        var andFilter = this.getElementIdFilter(elementTable.getIdField(), elementId);
        var joins = this.getTableJoins(elementTable, versionTable);
        var rows = this.repoData.readRepoTableRecords(elementTable.getRepoTable(), joins, allFields, andFilter, Collections.emptyList(), -1);

        if (rows.size() > 0) {
            return this.getElementFromRow(rows.get(0), elementClass);
        } else {
            throw new RepoException(String.format("element with ID '%s' not found", elementId));
        }
    }


    public ElementTable readElementTable(String elementClassName) throws RepoException {
        var table = this.repoMetaData.readTableStructure(elementClassName);
        return new ElementTable(table);
    }


    public VersionTable readVersionTable(ElementTable elementTable) throws RepoException {
        var versionTableName = elementTable.getIncomingRelations()
            .stream()
            .filter(rel -> rel.getBwdFieldName().toUpperCase().equals(VersionTable.ELEMENT_ID_COLNAME)) // TODO: make more generic
            .filter(rel -> rel.getBwdClassName().toUpperCase().endsWith(VersionTable.TABLE_SUFFIX)) // TODO: make more generic (e.g. check for von/bis fields)
            .map(RepoRelation::getBwdClassName)
            .findFirst()
            .orElse(null);

        if (versionTableName == null) {
            return null;
        } else {
            var table = this.repoMetaData.readTableStructure(versionTableName);
            return new VersionTable(table);
        }
    }


    public ElementRecord readElementRecord(ElementTable elementTable, long elementId, List<RepoField> fields) throws RepoException {
        var filter = new RowFilter(elementTable.getIdField(), RowFilterOperator.EQUALS, elementId);

        var records = this.repoData.readRepoTableRecords(elementTable.getRepoTable(), Collections.emptyList(), fields, List.of(filter), Collections.emptyList(), -1);
        if (records.size() != 1) {
            throw new IllegalArgumentException("multiple records for same id");
        }

        return new ElementRecord(records.get(0));
    }


    private List<RepoField> getFields(ElementTable elementTable, VersionTable versionTable, List<Denomination> denominationFields) {
        var elementDenominationFieldNames = this.getDenominationFieldNames(denominationFields, Denomination.ELEMENT_PATH);
        var elementFields = elementTable.getFields(elementDenominationFieldNames);
        // TODO: hack to ensure element id is always present
        if (!elementFields.contains(elementTable.getIdField())) {
            elementFields.add(elementTable.getIdField());
        }

        List<String> versionDenominationFieldNames = versionTable != null
            ? this.getDenominationFieldNames(denominationFields, Denomination.VERSION_PATH)
            : Collections.emptyList();
        List<RepoField> versionFields = versionTable != null
            ? versionTable.getFields(versionDenominationFieldNames)
            : Collections.emptyList();

        return Stream.concat(elementFields.stream(), versionFields.stream()).collect(Collectors.toList());
    }


    private List<String> getDenominationFieldNames(List<Denomination> denominationFields, String pathName) {
        return denominationFields
            .stream()
            .filter(field -> field.getPath().equals(pathName))
            .map(Denomination::getName)
            .collect(Collectors.toList());
    }


    private List<RowFilter> getQueryFilter(List<RepoField> fields, String query) {
        if (query.isEmpty()) {
            return Collections.emptyList();
        } else {
            return fields
                .stream()
                .map(f -> new RepoField(f.getTableName(), f.getName(), RepoFieldType.STRING, f.isId(), f.isNullable(), f.isUnique()))
                .map(f -> new RowFilter(f, RowFilterOperator.LIKE, JdbcRepoMetadataServiceImpl.WILDCARD + query + JdbcRepoMetadataServiceImpl.WILDCARD))
                .collect(Collectors.toList());
        }
    }


    private List<RowFilter> getElementIdFilter(RepoField elementIdField, String elementId) {
        var field = new RepoField(elementIdField.getTableName(), elementIdField.getName(), RepoFieldType.STRING, elementIdField.isId(), elementIdField.isNullable(), elementIdField.isUnique());
        var rowFilter = new RowFilter(field, RowFilterOperator.EQUALS, elementId);
        return List.of(rowFilter);
    }


    private List<RepoTableJoin> getTableJoins(ElementTable elementTable, VersionTable versionTable) {
        return versionTable != null
            ? List.of(new RepoTableJoin(elementTable.getRepoTable(), versionTable.getRepoTable(), elementTable.getIdField(), versionTable.getElementIdField()))
            : Collections.emptyList();
    }


    private ElementData getElementFromRow(RepoTableRecord row, ElementClass elementClass) {
        var elementId = row.findIdFieldValue().getValueString();
        var denominations = row.getFieldValues()
            .stream()
            .map(field -> new DenominationData(field.getName(), field.getValueString()))
            .collect(Collectors.toList());

        return new ElementData(elementClass, elementId, denominations);
    }
}
