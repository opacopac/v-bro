package com.tschanz.v_bro.app.presentation.viewmodel.element;

import com.tschanz.v_bro.app.presentation.viewmodel.common.IdItem;
import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementItem implements IdItem {
    private static final int MAX_NAME_LENGTH = 100;
    private final String id;
    private final String name;


    public static List<ElementItem> fromResponse(QueryElementsResponse response) {
        return response.getElements()
            .stream()
            .map(ElementItem::fromResponse)
            .collect(Collectors.toList());
    }


    public static ElementItem fromResponse(ElementResponse element) {
        var concatName = String.join( " - ", element.getDenominations());

        return new ElementItem(element.getId(), concatName);
    }


    @Override
    public String toString() {
        if (this.name.length() <= MAX_NAME_LENGTH) {
            return this.name;
        } else {
            return this.name.substring(0, MAX_NAME_LENGTH) + "...";
        }
    }
}
