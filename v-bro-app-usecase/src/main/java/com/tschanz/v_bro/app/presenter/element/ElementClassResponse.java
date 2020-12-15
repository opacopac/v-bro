package com.tschanz.v_bro.app.presenter.element;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ElementClassResponse {
    private final String name;


    public static ElementClassResponse fromDomain(ElementClass elementClass) {
        return new ElementClassResponse(elementClass.getName());
    }
}
