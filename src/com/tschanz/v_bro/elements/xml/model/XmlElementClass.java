package com.tschanz.v_bro.elements.xml.model;

import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.model.Denomination;

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

        Collection<String> existingNameFields = this.getExistingNameFields();
        Collection<Denomination> newDenominations = elementData.getFieldNames()
            .stream()
            .filter(fieldName -> !existingNameFields.contains(fieldName))
            .map(Denomination::new)
            .collect(Collectors.toList());

        this.denominations.addAll(newDenominations);
    }


    private Collection<String> getExistingNameFields() {
        return this.denominations
            .stream()
            .map(Denomination::getName)
            .collect(Collectors.toList());
    }
}
