package com.tschanz.v_bro.app.usecase.select_denominations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class SelectDenominationsRequest {
    private final List<String> selectedDenominationNames;
}
