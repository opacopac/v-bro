package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ElementItem implements IdItem {
    private static final int MAX_NAME_LENGTH = 100;

    private final String id;
    private final String name;


    @Override
    public String toString() {
        if (this.name.length() <= MAX_NAME_LENGTH) {
            return this.name;
        } else {
            return this.name.substring(0, MAX_NAME_LENGTH) + "...";
        }
    }
}
