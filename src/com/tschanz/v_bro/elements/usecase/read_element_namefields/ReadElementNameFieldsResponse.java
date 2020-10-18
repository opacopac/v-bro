package com.tschanz.v_bro.elements.usecase.read_element_namefields;

import java.util.List;


public class ReadElementNameFieldsResponse {
    public final List<ElementNameField> nameFields;


    public ReadElementNameFieldsResponse(List<ElementNameField> nameFields) {
        this.nameFields = nameFields;
    }


    public static class ElementNameField {
        public final String name;


        public ElementNameField(String name) {
            this.name = name;
        }
    }
}
