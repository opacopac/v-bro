package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionRecord;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcVersionService implements VersionService {
    @NonNull private final JdbcRepoService repo;
    @NonNull private final JdbcRepoDataService repoData;
    @NonNull private final JdbcElementService elementService;


    @Override
    public List<VersionData> readVersions(@NonNull ElementData element) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        var elementTable = this.elementService.readElementTable(element.getElementClass().getName());
        var versionTable = this.elementService.readVersionTable(elementTable);

        if (versionTable == null) {
            return List.of(VersionData.createEternal(element));
        }

        var idField = versionTable.getIdField();
        var versionRecords = this.repoData.readRepoTableRecords(
            versionTable.getRepoTable(),
            Collections.emptyList(),
            versionTable.getFields(List.of(idField.getName(), VersionTable.GUELTIG_VON_COLNAME, VersionTable.GUELTIG_BIS_COLNAME)),
            this.getRowFilters(versionTable, element.getId()),
            Collections.emptyList(),
            -1
        );
        var versions = versionRecords
            .stream()
            .map(row -> {
                String id = row.findIdFieldValue().getValueString();
                LocalDate gueltigVon = row.findFieldValue(VersionTable.GUELTIG_VON_COLNAME).getValueDate();
                LocalDate gueltigBis = row.findFieldValue(VersionTable.GUELTIG_BIS_COLNAME).getValueDate();
                Pflegestatus pflegestatus = (row.findFieldValue(VersionTable.PFLEGESTATUS_COLNAME) != null)
                    ? Pflegestatus.valueOf(row.findFieldValue(VersionTable.PFLEGESTATUS_COLNAME).getValueString())
                    : Pflegestatus.PRODUKTIV;

                return new VersionData(element, id, gueltigVon, gueltigBis, pflegestatus);
            })
            .collect(Collectors.toList());

        return versions;
    }


    public VersionRecord readVersionRecord(VersionTable versionTable, long versionId, List<RepoField> fields) throws RepoException {
        var filter = new RowFilter(versionTable.getIdField(), RowFilterOperator.EQUALS, versionId);

        List<RepoTableRecord> records = this.repoData.readRepoTableRecords(versionTable.getRepoTable(), Collections.emptyList(), fields, List.of(filter), Collections.emptyList(), -1);
        if (records.size() != 1) {
            throw new IllegalArgumentException("multiple records for same id");
        }

        return new VersionRecord(records.get(0));
    }


    private List<RowFilter> getRowFilters(VersionTable versionTable, String elementId) {
        var rowFilter = new RowFilter(
            versionTable.getElementIdField(),
            RowFilterOperator.EQUALS,
            Long.valueOf(elementId)
        );

        // TODO von/bis/pflegestatus

        return List.of(rowFilter);
    }
}
