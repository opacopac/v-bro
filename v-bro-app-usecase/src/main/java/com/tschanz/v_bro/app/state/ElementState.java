package com.tschanz.v_bro.app.state;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class ElementState {
    private String query = "";
    private String currentElementId = null;
}
