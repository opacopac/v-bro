package com.tschanz.v_bro.app.usecase.open_element;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class OpenElementRequest {
    private final String elementId;
    private final boolean autoOpenLastVersion;
}
