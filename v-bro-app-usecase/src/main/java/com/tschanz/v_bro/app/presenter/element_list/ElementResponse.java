package com.tschanz.v_bro.app.presenter.element_list;

import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ElementResponse {
    private final String id;
    private final List<String> names;


    public static ElementResponse fromDomain(ElementData elementData) {
        return new ElementResponse(
            elementData.getId(),
            elementData.getNameFieldValues()
                .stream()
                .map(DenominationData::getValue)
                .collect(Collectors.toList())
        );
    }
}
