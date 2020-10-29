package com.tschanz.v_bro.element_classes.persistence.xml.model;

import java.util.ArrayList;
import java.util.Collection;


public class XmlElementInfo {
    private final String elementId;
    private final String name;
    private final Collection<String> denominations = new ArrayList<>();


    public String getElementId() {
        return this.elementId;
    }

    public String getName() { return this.name; }

    public Collection<String> getDenominations() { return denominations; }


    public XmlElementInfo(String name, String elementId) {
        this.name = name;
        this.elementId = elementId;
    }


    public void addFieldName(String fieldName) {
        this.denominations.add(fieldName);
    }
}
