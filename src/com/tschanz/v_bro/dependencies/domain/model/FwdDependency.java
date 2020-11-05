package com.tschanz.v_bro.dependencies.domain.model;

import com.tschanz.v_bro.versions.domain.model.VersionData;

import java.util.List;


public class FwdDependency {
    protected final String elementClass;
    protected final String elementId;
    protected final List<VersionData> versions;


    public String elementName() { return this.elementClass; }
    public String elementId() { return this.elementId; }
    public List<VersionData> getVersions() { return versions; }


    public FwdDependency(String elementClass, String elementId, List<VersionData> versions) {
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versions = versions;
    }
}
