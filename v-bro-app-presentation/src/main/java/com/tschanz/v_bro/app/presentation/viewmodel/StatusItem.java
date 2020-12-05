package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public abstract class StatusItem {
    private final String statusText;


    public abstract boolean isError();
}
