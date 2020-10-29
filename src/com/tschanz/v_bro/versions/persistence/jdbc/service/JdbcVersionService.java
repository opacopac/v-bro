package com.tschanz.v_bro.versions.persistence.jdbc.service;

import com.tschanz.v_bro.elements.persistence.jdbc.service.JdbcElementService;
import com.tschanz.v_bro.repo.persistence.jdbc.model.*;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoData;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class JdbcVersionService implements VersionService {
    public final static String VERSION_TABLE_SUFFIX = "_V";
    public static String ELEMENT_ID_COLNAME = "ID_ELEMENT";
    public static String GUELTIG_VON_COLNAME = "GUELTIG_VON";
    public static String GUELTIG_BIS_COLNAME = "GUELTIG_BIS";
    public static String PFLEGESTATUS_COLNAME = "PFLEGEZYKLUS";

    private final JdbcRepoService repo;
    private final JdbcRepoMetadataService repoMetaData;
    private final JdbcRepoData repoData;
    private final JdbcElementService elementService;


    public JdbcVersionService(
        JdbcRepoService repo,
        JdbcRepoMetadataService repoMetaData,
        JdbcRepoData repoData,
        JdbcElementService elementService
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
        this.repoData = repoData;
        this.elementService = elementService;
    }


    @Override
    public List<VersionInfo> readVersionTimeline(String elementClass, String elementId) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        RepoTable elementTable = this.elementService.readElementTable(elementClass);
        RepoTable versionTable = this.readVersionTable(elementTable);
        RepoField idField = versionTable.findfirstIdField();

        List<RowInfo> versionRows = this.repoData.readData(
            versionTable.getName(),
            versionTable.findAllFields(idField.getName(), GUELTIG_VON_COLNAME, GUELTIG_BIS_COLNAME),
            this.getRowFilters(versionTable, elementId)
        );
        List<VersionInfo> versions = versionRows
            .stream()
            .map(row -> {
                String id = row.getFieldValue(idField.getName()).getValueString();
                LocalDate gueltigVon = row.getFieldValue(GUELTIG_VON_COLNAME).getValueDate();
                LocalDate gueltigBis = row.getFieldValue(GUELTIG_BIS_COLNAME).getValueDate();
                Pflegestatus pflegestatus = (row.getFieldValue(PFLEGESTATUS_COLNAME) != null)
                    ? Pflegestatus.valueOf(row.getFieldValue(PFLEGESTATUS_COLNAME).getValueString())
                    : Pflegestatus.PRODUKTIV;

                return new VersionInfo(id, gueltigVon, gueltigBis, pflegestatus);
            })
            .collect(Collectors.toList());

        return versions;
    }


    public RepoTable readVersionTable(RepoTable elementTable) throws RepoException {
        String versionTableName = elementTable.getIncomingRelations()
            .stream()
            .filter(rel -> rel.getBwdFieldName().toUpperCase().equals(ELEMENT_ID_COLNAME)) // TODO: make more generic
            .filter(rel -> rel.getBwdClassName().toUpperCase().endsWith(VERSION_TABLE_SUFFIX)) // TODO: make more generic (e.g. check for von/bis fields)
            .map(RepoRelation::getBwdClassName)
            .findFirst()
            .orElse(null);

        if (versionTableName == null) {
            throw new RepoException("version table not not for element table '" + elementTable.getName() + "'");
        } else {
            return this.repoMetaData.readTableStructure(versionTableName);
        }
    }


    private List<RowFilter> getRowFilters(RepoTable versionTable, String elementId) {
        FieldValue fieldValue = new FieldValue(
            versionTable.findField(ELEMENT_ID_COLNAME), // TODO: make more generic
            Long.valueOf(elementId)
        );
        RowFilter rowFilter = new RowFilter(RowFilterOperator.EQUALS, fieldValue);

        // TODO von/bis/pflegestatus

        return List.of(rowFilter);
    }
}
