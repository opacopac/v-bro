package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;


public class PflegestatusItem {
    private final Pflegestatus pflegestatus;


    public Pflegestatus getPflegestatus() { return pflegestatus; }


    public PflegestatusItem(Pflegestatus pflegestatus) {
        if (pflegestatus == null) {
            throw new IllegalArgumentException("pflegestatus must not be null");
        }

        this.pflegestatus = pflegestatus;
    }


    @Override
    public String toString() {
        return this.pflegestatus.name();
    }
}
