package com.tschanz.v_bro.app.presenter.element_class;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ElementClassResponseItem {
    private final String name;


    public static ElementClassResponseItem fromDomain(ElementClass elementClass) {
        return new ElementClassResponseItem(elementClass.getName());
    }
}
