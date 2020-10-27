package com.tschanz.v_bro.element_classes.usecase.read_element_denominations;

import java.util.List;


public class ReadElementDenominationsResponse {
    public final List<ElementDenomination> denominations;


    public ReadElementDenominationsResponse(List<ElementDenomination> denominations) {
        this.denominations = denominations;
    }


    public static class ElementDenomination {
        public final String name;


        public ElementDenomination(String name) {
            this.name = name;
        }
    }
}
