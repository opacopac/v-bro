package com.tschanz.v_bro.app.swing.model;


public class InfoStatusItem extends StatusItem {
    public boolean isError() { return false; }


    public InfoStatusItem(String statusText) {
        super(statusText);
    }
}
