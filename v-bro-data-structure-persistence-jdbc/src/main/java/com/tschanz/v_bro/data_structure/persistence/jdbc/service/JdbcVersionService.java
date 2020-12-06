package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.repo.persistence.jdbc.model.*;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcVersionService implements VersionService {
    public final static String VERSION_TABLE_SUFFIX = "_V";
    public static String ELEMENT_ID_COLNAME = "ID_ELEMENT";
    public static String GUELTIG_VON_COLNAME = "GUELTIG_VON";
    public static String GUELTIG_BIS_COLNAME = "GUELTIG_BIS";
    public static String PFLEGESTATUS_COLNAME = "PFLEGEZYKLUS";
    @NonNull private final JdbcRepoService repo;
    @NonNull private final JdbcRepoMetadataService repoMetaData;
    @NonNull private final JdbcRepoDataService repoData;
    @NonNull private final JdbcElementService elementService;


    @Override
    public List<VersionData> readVersionTimeline(@NonNull String elementClass, @NonNull String elementId) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        var elementTable = this.elementService.readElementTable(elementClass);
        var versionTable = this.readVersionTable(elementTable);

        if (versionTable == null) {
            return List.of(VersionData.ETERNAL_VERSION);
        }

        var idField = versionTable.findfirstIdField();
        var versionRecords = this.repoData.readRepoTableRecords(
            versionTable,
            versionTable.findAllFields(idField.getName(), GUELTIG_VON_COLNAME, GUELTIG_BIS_COLNAME),
            this.getRowFilters(versionTable, elementId),
            Collections.emptyList(),
            -1
        );
        var versions = versionRecords
            .stream()
            .map(row -> {
                String id = row.findIdFieldValue().getValueString();
                LocalDate gueltigVon = row.findFieldValue(GUELTIG_VON_COLNAME).getValueDate();
                LocalDate gueltigBis = row.findFieldValue(GUELTIG_BIS_COLNAME).getValueDate();
                Pflegestatus pflegestatus = (row.findFieldValue(PFLEGESTATUS_COLNAME) != null)
                    ? Pflegestatus.valueOf(row.findFieldValue(PFLEGESTATUS_COLNAME).getValueString())
                    : Pflegestatus.PRODUKTIV;

                return new VersionData(id, gueltigVon, gueltigBis, pflegestatus);
            })
            .collect(Collectors.toList());

        return versions;
    }


    public RepoTable readVersionTable(RepoTable elementTable) throws RepoException {
        var versionTableName = elementTable.getIncomingRelations()
            .stream()
            .filter(rel -> rel.getBwdFieldName().toUpperCase().equals(ELEMENT_ID_COLNAME)) // TODO: make more generic
            .filter(rel -> rel.getBwdClassName().toUpperCase().endsWith(VERSION_TABLE_SUFFIX)) // TODO: make more generic (e.g. check for von/bis fields)
            .map(RepoRelation::getBwdClassName)
            .findFirst()
            .orElse(null);

        if (versionTableName == null) {
            return null;
        } else {
            return this.repoMetaData.readTableStructure(versionTableName);
        }
    }


    private List<RowFilter> getRowFilters(RepoTable versionTable, String elementId) {
        var rowFilter = new RowFilter(
            versionTable.findField(ELEMENT_ID_COLNAME), // TODO: make more generic
            RowFilterOperator.EQUALS,
            Long.valueOf(elementId)
        );

        // TODO von/bis/pflegestatus

        return List.of(rowFilter);
    }
}
