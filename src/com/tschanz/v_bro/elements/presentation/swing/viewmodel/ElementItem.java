package com.tschanz.v_bro.elements.presentation.swing.viewmodel;


public class ElementItem {
    private final String id;
    private final String name;


    public String getId() { return id; }


    public ElementItem(String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}