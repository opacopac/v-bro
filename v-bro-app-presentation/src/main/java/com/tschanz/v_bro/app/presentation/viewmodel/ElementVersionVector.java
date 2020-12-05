package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ElementVersionVector {
    private final String elementClass;
    private final String elementId;
    private final String versionId;
}
