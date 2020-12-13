package com.tschanz.v_bro.app.usecase.read_denominations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ReadDenominationRequest {
    private final String elementClassName;
    private final boolean autoSelectFirstDenomination;
}
