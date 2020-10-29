package com.tschanz.v_bro.version_aggregates.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.persistence.jdbc.service.JdbcVersionService;

import java.util.List;


public class JdbcVersionAggregate extends VersionAggregate {
    public JdbcVersionAggregate(
        RepoTableRecord elementRecord,
        RepoTableRecord versionRecord,
        List<JdbcAggregateNode> versionChildNodes
    ) {
        super(
            getVersionInfo(versionRecord),
            getRootNode(elementRecord, versionRecord, versionChildNodes)
        );
    }


    private static VersionInfo getVersionInfo(RepoTableRecord versionRecord) {
        FieldValue statusField = versionRecord.findFieldValue(JdbcVersionService.PFLEGESTATUS_COLNAME);
        Pflegestatus pflegestatus = statusField != null ? Pflegestatus.valueOf(statusField.getValueString()) : Pflegestatus.PRODUKTIV;

        return new VersionInfo(
            versionRecord.findIdFieldValue().getValueString(),
            versionRecord.findFieldValue(JdbcVersionService.GUELTIG_VON_COLNAME).getValueDate(),
            versionRecord.findFieldValue(JdbcVersionService.GUELTIG_BIS_COLNAME).getValueDate(),
            pflegestatus
        );
    }


    private static JdbcAggregateNode getRootNode(
        RepoTableRecord elementRecord,
        RepoTableRecord versionRecord,
        List<JdbcAggregateNode> versionChildNodes
    ) {
        return new JdbcAggregateNode(
            elementRecord,
            List.of(new JdbcAggregateNode(versionRecord, versionChildNodes))
        );
    }
}
