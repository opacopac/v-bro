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
        List<AggregateDataNode> elementChildNodes,
        List<AggregateDataNode> versionChildNodes
    ) {
        super(
            getRootNode(elementRecord, versionRecord, elementChildNodes, versionChildNodes)
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
        List<AggregateDataNode> elementChildNodes,
        List<AggregateDataNode> versionChildNodes
    ) {
        List<AggregateDataNode> childNodes = new ArrayList<>(elementChildNodes);
        if (versionRecord != null) {
            childNodes.add(new AggregateDataNode(versionRecord.getRecord(), versionChildNodes));
        }

        return new AggregateDataNode(elementRecord.getRecord(), childNodes);
    }
}
