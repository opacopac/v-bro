package com.tschanz.v_bro.elements.domain.model;

import java.util.ArrayList;
import java.util.List;


public class ElementClass {
    protected final String name;
    protected final List<NameField> nameFields = new ArrayList<>();


    public String getName() { return this.name; }
    public List<NameField> getNameFields() { return this.nameFields; }


    public ElementClass(String name) {
        this.name = name;
    }
}
