package com.tschanz.v_bro.app.presenter.element_list;

import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementListResponse {
    private final SelectedList<ElementResponse> selectedElementList;


    public static ElementListResponse fromDomain(SelectedList<ElementData> elementDataList) {
        var elements = elementDataList.getItems()
            .stream()
            .map(ElementResponse::fromDomain)
            .collect(Collectors.toList());
        var selectedElement = elementDataList.getSelectedItem() != null
            ? ElementResponse.fromDomain(elementDataList.getSelectedItem())
            : null;
        var slist = new SelectedList<>(elements, selectedElement);

        return new ElementListResponse(slist);
    }
}
