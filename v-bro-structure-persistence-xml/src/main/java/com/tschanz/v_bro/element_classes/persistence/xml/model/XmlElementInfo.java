package com.tschanz.v_bro.element_classes.persistence.xml.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;


@RequiredArgsConstructor
public class XmlElementInfo {
    @Getter private final String elementId;
    @Getter private final String name;
    @Getter private final Collection<String> denominations = new ArrayList<>();


    public void addFieldName(String fieldName) {
        this.denominations.add(fieldName);
    }
}
