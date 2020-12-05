package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class PflegestatusItem {
    @NonNull private final Pflegestatus pflegestatus;


    @Override
    public String toString() {
        return this.pflegestatus.name();
    }
}
