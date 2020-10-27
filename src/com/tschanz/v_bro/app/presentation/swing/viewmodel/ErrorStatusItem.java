package com.tschanz.v_bro.app.presentation.swing.viewmodel;


public class ErrorStatusItem extends StatusItem {
    public boolean isError() { return true; }


    public ErrorStatusItem(String statusText) {
        super(statusText);
    }
}
