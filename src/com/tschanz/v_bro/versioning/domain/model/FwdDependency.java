package com.tschanz.v_bro.versioning.domain.model;


public class FwdDependency {
    protected final String elementName;
    protected final String elementId;


    public String elementName() { return this.elementName; }
    public String elementId() { return this.elementId; }


    public FwdDependency(String elementName, String elementId) {
        this.elementName = elementName;
        this.elementId = elementId;
    }
}
