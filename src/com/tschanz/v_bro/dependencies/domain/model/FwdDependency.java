package com.tschanz.v_bro.dependencies.domain.model;

import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import java.util.List;


public class FwdDependency {
    protected final String elementClass;
    protected final String elementId;
    protected final List<VersionInfo> versions;


    public String elementName() { return this.elementClass; }
    public String elementId() { return this.elementId; }
    public List<VersionInfo> getVersions() { return versions; }


    public FwdDependency(String elementClass, String elementId, List<VersionInfo> versions) {
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versions = versions;
    }
}
