package com.tschanz.v_bro.dependencies.presentation.swing.viewmodel;

import com.tschanz.v_bro.versions.presentation.swing.viewmodel.VersionItem;

import java.util.List;


public class FwdDependencyItem {
    protected final String elementClass;
    protected final String elementId;
    protected final List<VersionItem> versions;


    public String elementName() { return this.elementClass; }
    public String elementId() { return this.elementId; }
    public List<VersionItem> getVersions() { return versions; }


    public FwdDependencyItem(String elementClass, String elementId, List<VersionItem> versions) {
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versions = versions;
    }
}
