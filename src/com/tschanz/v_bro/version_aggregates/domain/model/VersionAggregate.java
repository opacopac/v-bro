package com.tschanz.v_bro.version_aggregates.domain.model;


import com.tschanz.v_bro.versions.domain.model.VersionData;

public class VersionAggregate {
    protected final VersionData versionData;
    protected final AggregateNode rootNode;


    public VersionData getVersionInfo() { return this.versionData; }
    public AggregateNode getRootNode() { return rootNode; }


    public VersionAggregate(VersionData versionData, AggregateNode rootNode) {
        this.versionData = versionData;
        this.rootNode = rootNode;
    }
}
