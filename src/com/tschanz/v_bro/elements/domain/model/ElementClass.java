package com.tschanz.v_bro.elements.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ElementClass {
    protected final String name;
    protected final List<String> nameFields = new ArrayList<>();


    public String getName() { return this.name; }
    public Collection<String> getNameFields() { return this.nameFields; }


    public ElementClass(String name) {
        this.name = name;
    }
}
