package com.tschanz.v_bro.elements.domain.model;


public class NameFieldData {
    protected final String name;
    protected final String value;


    public String getName() { return this.name; }
    public String getValue() { return this.value; }


    public NameFieldData(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
