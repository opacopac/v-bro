package com.tschanz.v_bro.elements.domain.model;


public class DenominationData {
    protected final String name;
    protected final String value;


    public String getName() { return this.name; }
    public String getValue() { return this.value; }


    public DenominationData(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
