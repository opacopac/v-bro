package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DenominationService;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.ElementTable;
import com.tschanz.v_bro.data_structure.persistence.jdbc.model.VersionTable;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class JdbcDenominationService implements DenominationService {
    private final static List<String> NO_DENOMINATION_NAME = List.of("VERSIONID", "CREATED_BY", "MODIFIED_BY", "BEMERKUNG", VersionTable.PFLEGESTATUS_COLNAME, VersionTable.ELEMENT_ID_COLNAME); // TODO => app config
    private final static List<RepoFieldType> NO_DENOMINATION_TYPE = List.of(RepoFieldType.BOOL, RepoFieldType.DATE, RepoFieldType.TIMESTAMP);
    private final JdbcElementService elementService;


    @Override
    public List<Denomination> readDenominations(@NonNull ElementClass elementClass) throws RepoException {
        ElementTable elementTable = this.elementService.readElementTable(elementClass.getName());
        VersionTable versionTable = this.elementService.readVersionTable(elementTable);
        List<RepoField> elementTableFields = elementTable.getFields();
        List<RepoField> versionTableFields = versionTable != null ? versionTable.getFields() : Collections.emptyList();

        var denominations = this.getDenominations(Denomination.ELEMENT_PATH, elementTableFields);
        if (versionTable != null) {
            denominations.addAll(this.getDenominations(Denomination.VERSION_PATH, versionTableFields));
        }

        return denominations;
    }


    private List<Denomination> getDenominations(String container, List<RepoField> elementTableFields) {
        return elementTableFields
            .stream()
            .filter(dbField -> !NO_DENOMINATION_NAME.contains(dbField.getName()))
            .filter(dbField -> !NO_DENOMINATION_TYPE.contains(dbField.getType()))
            //.sorted(new DenominationFieldComparator())
            .map(dbField -> new Denomination(container, dbField.getName()))
            .collect(Collectors.toList());
    }
}
