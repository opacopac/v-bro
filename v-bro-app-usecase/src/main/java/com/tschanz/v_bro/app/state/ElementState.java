package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

import java.util.Collections;
import java.util.List;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class ElementState {
    private String query = "";
    @NonNull private List<ElementData> queryResult = Collections.emptyList();
    private ElementData currentElement = null;


    public String getCurrentElementId() {
        return this.currentElement != null
            ? this.currentElement.getId()
            : null;
    }
}
