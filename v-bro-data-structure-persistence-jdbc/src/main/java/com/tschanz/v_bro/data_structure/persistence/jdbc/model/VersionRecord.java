package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VersionRecord {
    private final RepoTableRecord record;


    public FieldValue getIdFieldValue() {
        return this.record.findIdFieldValue();
    }


    public VersionData createVersion(ElementData element) {
        FieldValue statusField = this.record.findFieldValue(VersionTable.PFLEGESTATUS_COLNAME);
        Pflegestatus pflegestatus = statusField != null ? Pflegestatus.valueOf(statusField.getValueString()) : Pflegestatus.PRODUKTIV;

        return new VersionData(
            element,
            this.record.findIdFieldValue().getValueString(),
            this.record.findFieldValue(VersionTable.GUELTIG_VON_COLNAME).getValueDate(),
            this.record.findFieldValue(VersionTable.GUELTIG_BIS_COLNAME).getValueDate(),
            pflegestatus
        );
    }
}

