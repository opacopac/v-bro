package com.tschanz.v_bro.app.usecase.open_element_class;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class OpenElementClassRequest {
    private final String elementClassName;
}
