package com.tschanz.v_bro.elements.persistence.xml.model;

import java.util.ArrayList;
import java.util.Collection;


public class XmlElementStructurePart {
    private final String elementId;
    private final String name;
    private final Collection<String> fieldNames = new ArrayList<>();


    public String getElementId() {
        return this.elementId;
    }

    public String getName() { return this.name; }

    public Collection<String> getFieldNames() { return fieldNames; }


    public XmlElementStructurePart(String name, String elementId) {
        this.name = name;
        this.elementId = elementId;
    }


    public void addFieldName(String fieldName) {
        this.fieldNames.add(fieldName);
    }
}
