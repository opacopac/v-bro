package com.tschanz.v_bro.structure.persistence.xml.model;

import com.tschanz.v_bro.structure.domain.model.ElementClass;
import com.tschanz.v_bro.structure.domain.model.Denomination;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class XmlElementClass extends ElementClass {
    @Getter private final HashSet<String> dataIds = new HashSet<>();
    protected final List<Denomination> denominations = new ArrayList<>();


    public XmlElementClass(String name) {
        super(name);
    }


    public void addElement(XmlElementInfo elementData) {
        this.dataIds.add(elementData.getElementId());

        Collection<String> existingNameFields = this.getExistingDenominations();
        Collection<Denomination> newDenominations = elementData.getDenominations()
            .stream()
            .filter(fieldName -> !existingNameFields.contains(fieldName))
            .map(Denomination::new)
            .collect(Collectors.toList());

        this.denominations.addAll(newDenominations);
    }


    private Collection<String> getExistingDenominations() {
        return this.denominations
            .stream()
            .map(Denomination::getName)
            .collect(Collectors.toList());
    }
}
