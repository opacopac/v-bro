package com.tschanz.v_bro.elements.domain.model;

import java.util.ArrayList;
import java.util.List;


public class ElementData {
    protected final String id;
    protected final List<DenominationData> nameFieldValues = new ArrayList<>();


    public String getId() { return this.id; }
    public List<DenominationData> getNameFieldValues() { return this.nameFieldValues; }


    public ElementData(String id) {
        this.id = id;
    }


    public ElementData(String id, List<DenominationData> nameFieldValues) {
        this.id = id;
        this.nameFieldValues.addAll(nameFieldValues);
    }


    public void addNameField(DenominationData nameField) {
        this.nameFieldValues.add(nameField);
    }
}
