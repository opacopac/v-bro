package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionRecord;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilterOperator;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcVersionService implements VersionService {
    @NonNull private final JdbcRepoDataService repoDataService;
    @NonNull private final JdbcElementService elementService;


    @Override
    @SneakyThrows
    public List<VersionData> readVersions(
        @NonNull ElementData element,
        @NonNull LocalDate timelineVon,
        @NonNull LocalDate timelineBis,
        @NonNull Pflegestatus minPflegestatus
    ) {
        var elementTable = this.elementService.readElementTable(element.getElementClass().getName());
        var versionTable = this.elementService.readVersionTable(elementTable);

        if (versionTable == null) {
            return List.of(VersionData.createEternal(element));
        }

        var versionRecords = this.repoDataService.readRepoTableRecords(
            versionTable.getRepoTable(),
            Collections.emptyList(),
            List.of(versionTable.getIdField(), versionTable.getGueltigVonField(), versionTable.getGueltigBisField(), versionTable.getPflegestatusField()),
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

        return versions // TODO: filter when reading from db
            .stream()
            .filter(v -> v.getGueltigBis().isAfter(timelineVon) || v.getGueltigBis().isEqual(timelineVon))
            .filter(v -> v.getGueltigVon().isBefore(timelineBis) || v.getGueltigVon().isEqual(timelineBis))
            .filter(v -> v.getPflegestatus().isHigherOrEqual(minPflegestatus))
            .collect(Collectors.toList());
    }


    @SneakyThrows
    public VersionRecord readVersionRecord(VersionTable versionTable, long versionId, List<RepoField> fields) {
        var filter = new RowFilter(versionTable.getIdField(), RowFilterOperator.EQUALS, versionId);

        List<RepoTableRecord> records = this.repoDataService.readRepoTableRecords(versionTable.getRepoTable(), Collections.emptyList(), fields, List.of(filter), Collections.emptyList(), -1);
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
