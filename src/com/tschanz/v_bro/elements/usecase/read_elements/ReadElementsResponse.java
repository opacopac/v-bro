package com.tschanz.v_bro.elements.usecase.read_elements;

import java.util.List;


public class ReadElementsResponse {
    public final List<Element> elements;


    public ReadElementsResponse(List<Element> elements) {
        this.elements = elements;
    }


    public static class Element {
        public final String id;
        public final String name; // TBD => name fields


        public Element(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
