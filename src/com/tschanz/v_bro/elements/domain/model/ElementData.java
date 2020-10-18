package com.tschanz.v_bro.elements.domain.model;

import java.util.ArrayList;
import java.util.List;


public class ElementData {
    protected final String id;
    protected final List<NameFieldData> nameFieldValues = new ArrayList<>();


    public String getId() { return this.id; }
    public List<NameFieldData> getNameFieldValues() { return this.nameFieldValues; }


    public ElementData(String id) {
        this.id = id;
    }


    public ElementData(String id, List<NameFieldData> nameFieldValues) {
        this.id = id;
        this.nameFieldValues.addAll(nameFieldValues);
    }


    public void addNameField(NameFieldData nameField) {
        this.nameFieldValues.add(nameField);
    }
}
