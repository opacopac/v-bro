package com.tschanz.v_bro.elements.domain.model;

import java.util.ArrayList;
import java.util.List;


public class ElementData {
    protected final String id;
    protected final List<NameFieldData> nameFields = new ArrayList<>();


    public String getId() { return this.id; }
    public List<NameFieldData> getNameFields() { return this.nameFields; }


    public ElementData(String id) {
        this.id = id;
    }


    public ElementData(String id, List<NameFieldData> nameFields) {
        this.id = id;
        this.nameFields.addAll(nameFields);
    }


    public void addNameField(NameFieldData nameField) {
        this.nameFields.add(nameField);
    }
}
