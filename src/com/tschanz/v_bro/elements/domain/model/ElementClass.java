package com.tschanz.v_bro.elements.domain.model;

import java.util.ArrayList;
import java.util.List;


public class ElementClass {
    protected final String name;
    protected final List<Denomination> denominations = new ArrayList<>();


    public String getName() { return this.name; }
    public List<Denomination> getNameFields() { return this.denominations; }


    public ElementClass(String name) {
        this.name = name;
    }
}
