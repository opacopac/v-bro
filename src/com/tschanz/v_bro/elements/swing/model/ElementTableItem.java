package com.tschanz.v_bro.elements.swing.model;


public class ElementTableItem {
    private final String name;

    public String getName() { return name; }


    public ElementTableItem(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.getName();
    }
}
