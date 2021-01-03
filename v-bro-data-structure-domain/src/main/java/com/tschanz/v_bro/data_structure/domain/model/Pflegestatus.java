package com.tschanz.v_bro.data_structure.domain.model;


public enum Pflegestatus {
    PRODUKTIV,
    ABNAHME,
    TEST,
    IN_ARBEIT;


    public boolean isHigherOrEqual(Pflegestatus pflegestatus) {
        return this.compareTo(pflegestatus) <= 0;
    }
}
