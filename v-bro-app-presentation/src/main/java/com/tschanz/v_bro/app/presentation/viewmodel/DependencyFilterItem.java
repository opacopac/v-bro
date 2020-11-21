package com.tschanz.v_bro.app.presentation.viewmodel;


public class DependencyFilterItem {
    public boolean isFwd;



    public DependencyFilterItem(boolean isFwd) {
        this.isFwd = isFwd;
    }


    @Override
    public String toString() {
        return this.isFwd ? "FWD" : "BWD";
    }
}
