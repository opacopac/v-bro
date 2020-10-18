package com.tschanz.v_bro.elements.usecase.read_element_classes;

import java.util.List;


public class ReadElementClassesResponse {
    public final List<String> elementTableNames;


    public ReadElementClassesResponse(List<String> elementTableNames) {
        this.elementTableNames = elementTableNames;
    }
}
