package com.tschanz.v_bro.elements.xml.model;

import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.model.NameField;

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
        Collection<NameField> newNameFields = elementData.getFieldNames()
            .stream()
            .filter(fieldName -> !existingNameFields.contains(fieldName))
            .map(NameField::new)
            .collect(Collectors.toList());

        this.nameFields.addAll(newNameFields);
    }


    private Collection<String> getExistingNameFields() {
        return this.nameFields
            .stream()
            .map(NameField::getName)
            .collect(Collectors.toList());
    }
}
