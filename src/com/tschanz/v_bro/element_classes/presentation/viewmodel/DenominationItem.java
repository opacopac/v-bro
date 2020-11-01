package com.tschanz.v_bro.element_classes.presentation.viewmodel;


public class DenominationItem {
    public static final int MAX_DENOMINATIONS = 5;


    private final String name;


    public String getName() { return name; }


    public DenominationItem(String name) {
        this.name = name;
    }
}
