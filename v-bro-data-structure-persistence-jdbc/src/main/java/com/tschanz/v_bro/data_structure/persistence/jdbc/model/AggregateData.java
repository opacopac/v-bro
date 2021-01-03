package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class AggregateData extends VersionAggregate {
    @Getter private final ElementRecord elementRecord;
    @Getter private final VersionRecord versionRecord;


    public AggregateData(
        ElementRecord elementRecord,
        VersionRecord versionRecord,
        List<AggregateDataNode> versionChildNodes
    ) {
        super(
            getRootNode(elementRecord, versionRecord, versionChildNodes)
        );

        this.elementRecord = elementRecord;
        this.versionRecord = versionRecord;
    }


    public List<RepoTableRecord> getAllRecords() {
        return this.getAllRecordsAtNode((AggregateDataNode) this.getRootNode());
    }


    private List<RepoTableRecord> getAllRecordsAtNode(AggregateDataNode aggregateDataNode) {
        ArrayList<RepoTableRecord> nodes = new ArrayList<>();

        nodes.add(aggregateDataNode.getRepoTableRecord());
        aggregateDataNode.getJdbcChildNodes().forEach(childNode -> nodes.addAll(this.getAllRecordsAtNode(childNode)));

        return nodes;
    }


    private static AggregateDataNode getRootNode(
        ElementRecord elementRecord,
        VersionRecord versionRecord,
        List<AggregateDataNode> versionChildNodes
    ) {
        return new AggregateDataNode(
            elementRecord.getRecord(),
            List.of(new AggregateDataNode(versionRecord != null ? versionRecord.getRecord() : elementRecord.getRecord(), versionChildNodes))
        );
    }
}
