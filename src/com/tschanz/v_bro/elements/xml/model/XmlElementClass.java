package com.tschanz.v_bro.elements.xml.model;

import com.tschanz.v_bro.elements.domain.model.ElementClass;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;


public class XmlElementClass extends ElementClass {
    private final HashSet<String> dataIds = new HashSet<>();


    public HashSet<String> getDataIds() { return this.dataIds; }


    public XmlElementClass(String name) {
        super(name);
    }


    public void addElementData(XmlElementStructurePart elementData) {
        this.dataIds.add(elementData.getElementId());

        Collection<String> distinctFieldNames = elementData.getFieldNames()
            .stream()
            .filter(field -> !this.nameFields.contains(field))
            .collect(Collectors.toList());

        this.nameFields.addAll(distinctFieldNames);
    }
}
