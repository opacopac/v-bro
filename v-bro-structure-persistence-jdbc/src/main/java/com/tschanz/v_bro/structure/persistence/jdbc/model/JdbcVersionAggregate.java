package com.tschanz.v_bro.structure.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.structure.domain.model.VersionData;
import com.tschanz.v_bro.structure.persistence.jdbc.service.JdbcVersionService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class JdbcVersionAggregate extends VersionAggregate {
    @Getter private final RepoTableRecord elementRecord;
    @Getter private final RepoTableRecord versionRecord;


    public JdbcVersionAggregate(
        RepoTableRecord elementRecord,
        RepoTableRecord versionRecord,
        List<JdbcAggregateNode> versionChildNodes
    ) {
        super(
            versionRecord != null ? getVersionInfo(versionRecord) : VersionData.ETERNAL_VERSION,
            getRootNode(elementRecord, versionRecord, versionChildNodes)
        );

        this.elementRecord = elementRecord;
        this.versionRecord = versionRecord;
    }


    public List<RepoTableRecord> getAllRecords() {
        return this.getAllRecordsAtNode((JdbcAggregateNode) this.getRootNode());
    }


    private List<RepoTableRecord> getAllRecordsAtNode(JdbcAggregateNode jdbcAggregateNode) {
        ArrayList<RepoTableRecord> nodes = new ArrayList<>();

        nodes.add(jdbcAggregateNode.getRepoTableRecord());
        jdbcAggregateNode.getJdbcChildNodes().forEach(childNode -> nodes.addAll(this.getAllRecordsAtNode(childNode)));

        return nodes;
    }



    private static VersionData getVersionInfo(RepoTableRecord versionRecord) {
        FieldValue statusField = versionRecord.findFieldValue(JdbcVersionService.PFLEGESTATUS_COLNAME);
        Pflegestatus pflegestatus = statusField != null ? Pflegestatus.valueOf(statusField.getValueString()) : Pflegestatus.PRODUKTIV;

        return new VersionData(
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
            List.of(new JdbcAggregateNode(versionRecord != null ? versionRecord : elementRecord, versionChildNodes))
        );
    }

}
