package com.tschanz.v_bro.app.presentation.viewmodel;


public class DenominationItem implements IdItem {
    public static final int MAX_DENOMINATIONS = 5;


    private final String name;


    public String getName() { return name; }
    public String getId() { return name; }


    public DenominationItem(String name) {
        this.name = name;
    }
}
