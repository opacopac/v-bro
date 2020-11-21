package com.tschanz.v_bro.app.presentation.viewmodel;


public abstract class StatusItem {
    private final String statusText;


    public String getStatusText() { return statusText; }
    public abstract boolean isError();


    public StatusItem(String statusText) {
        this.statusText = statusText;
    }
}
