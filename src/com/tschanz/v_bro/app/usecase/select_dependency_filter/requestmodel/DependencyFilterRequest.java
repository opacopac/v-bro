package com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel;


public class DependencyFilterRequest {
    public final boolean isFwd;


    public DependencyFilterRequest(boolean isFwd) {
        this.isFwd = isFwd;
    }
}
