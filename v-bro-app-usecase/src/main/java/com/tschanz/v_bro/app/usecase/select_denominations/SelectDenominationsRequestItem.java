package com.tschanz.v_bro.app.usecase.select_denominations;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SelectDenominationsRequestItem {
    private final String path;
    private final String name;


    public Denomination toDomain() {
        return new Denomination(this.path, this.name);
    }
}
