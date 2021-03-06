package com.tschanz.v_bro.data_structure.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@EqualsAndHashCode
@RequiredArgsConstructor
public class Denomination {
    public static final String ELEMENT_PATH = "E"; // TODO: just temp
    public static final String VERSION_PATH = "V"; // TODO: just temp
    @Getter protected final String path; // TODO: just temp
    @Getter protected final String name;


    public boolean isElementId() {
        return this.path.equals(ELEMENT_PATH) && this.name.toLowerCase().contains("id"); // TODO: temp hack => configure when connecting to repo
    }
}
