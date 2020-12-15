package com.tschanz.v_bro.app.usecase.query_elements;

import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class QueryElementsResponse {
    private final List<ElementResponse> elements;


    public static QueryElementsResponse fromDomain(List<ElementData> elementDataList) {
        var elements = elementDataList
            .stream()
            .map(ElementResponse::fromDomain)
            .collect(Collectors.toList());

        return new QueryElementsResponse(elements);
    }
}
