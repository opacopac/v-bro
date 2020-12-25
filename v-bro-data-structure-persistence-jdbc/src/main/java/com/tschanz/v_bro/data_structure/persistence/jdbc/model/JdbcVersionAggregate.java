package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class JdbcVersionAggregate extends VersionAggregate {
    @Getter private final ElementRecord elementRecord;
    @Getter private final VersionRecord versionRecord;


    public JdbcVersionAggregate(
        ElementData element,
        ElementRecord elementRecord,
        VersionRecord versionRecord,
        List<JdbcAggregateNode> versionChildNodes
    ) {
        super(
            versionRecord != null ? versionRecord.createVersion(element) : VersionData.createEternal(element),
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


    private static JdbcAggregateNode getRootNode(
        ElementRecord elementRecord,
        VersionRecord versionRecord,
        List<JdbcAggregateNode> versionChildNodes
    ) {
        return new JdbcAggregateNode(
            elementRecord.getRecord(),
            List.of(new JdbcAggregateNode(versionRecord != null ? versionRecord.getRecord() : elementRecord.getRecord(), versionChildNodes))
        );
    }
}
