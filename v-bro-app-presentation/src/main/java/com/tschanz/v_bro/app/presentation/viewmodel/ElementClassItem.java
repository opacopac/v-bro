package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ElementClassItem implements IdItem {
    private final String name;


    @Override
    public String getId() {
        return this.name;
    }


    @Override
    public String toString() {
        return this.getName();
    }
}
