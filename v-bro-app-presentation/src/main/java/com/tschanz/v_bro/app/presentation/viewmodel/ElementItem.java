package com.tschanz.v_bro.app.presentation.viewmodel;


public class ElementItem implements IdItem {
    private static final int MAX_NAME_LENGTH = 100;
    private final String id;
    private final String name;


    public String getId() { return id; }


    public ElementItem(String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String toString() {
        if (this.name.length() <= MAX_NAME_LENGTH) {
            return this.name;
        } else {
            return this.name.substring(0, MAX_NAME_LENGTH) + "...";
        }
    }
}
