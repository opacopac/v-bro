package com.tschanz.v_bro.data_structure.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class ElementData {
    @Getter protected final String id;
    @Getter protected final List<DenominationData> nameFieldValues = new ArrayList<>();


    public ElementData(String id, List<DenominationData> nameFieldValues) {
        this.id = id;
        this.nameFieldValues.addAll(nameFieldValues);
    }


    public void addNameField(DenominationData nameField) {
        this.nameFieldValues.add(nameField);
    }
}
