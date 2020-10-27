package com.tschanz.v_bro.element_classes.presentation.swing.viewmodel;


public class ElementClassItem {
    private final String name;

    public String getName() { return name; }


    public ElementClassItem(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.getName();
    }
}