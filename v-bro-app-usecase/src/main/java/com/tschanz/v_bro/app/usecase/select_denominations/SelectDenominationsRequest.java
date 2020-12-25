package com.tschanz.v_bro.app.usecase.select_denominations;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class SelectDenominationsRequest {
    private final List<SelectDenominationsRequestItem> selectedDenominations;


    public List<Denomination> toDomain() {
        return this.selectedDenominations
            .stream()
            .map(SelectDenominationsRequestItem::toDomain)
            .collect(Collectors.toList());
    }
}
