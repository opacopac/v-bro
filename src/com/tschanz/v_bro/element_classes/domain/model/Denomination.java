package com.tschanz.v_bro.element_classes.domain.model;

import java.util.Objects;


public class Denomination {
    protected final String name;


    public String getName() { return this.name; }


    public Denomination(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Denomination that = (Denomination) o;
        return Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
