package com.tschanz.v_bro.versioning.domain.model;


public class VersionAggregate {
    protected final VersionInfo versionInfo;
    protected final AggregateNode rootNode;


    public VersionInfo getVersionInfo() { return this.versionInfo; }
    public AggregateNode getRootNode() { return rootNode; }


    public VersionAggregate(VersionInfo versionInfo, AggregateNode rootNode) {
        this.versionInfo = versionInfo;
        this.rootNode = rootNode;
    }
}
