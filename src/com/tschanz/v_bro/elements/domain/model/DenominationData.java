package com.tschanz.v_bro.elements.domain.model;


import java.util.Objects;

public class DenominationData {
    protected final String name;
    protected final String value;


    public String getName() { return this.name; }
    public String getValue() { return this.value; }


    public DenominationData(String name, String value) {
        this.name = name;
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenominationData that = (DenominationData) o;
        return Objects.equals(name, that.name);
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
