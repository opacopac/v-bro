package com.tschanz.v_bro.app.presentation.swing.viewmodel;


public class InfoStatusItem extends StatusItem {
    public boolean isError() { return false; }


    public InfoStatusItem(String statusText) {
        super(statusText);
    }
}
