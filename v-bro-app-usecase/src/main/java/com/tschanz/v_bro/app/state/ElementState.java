package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
public class ElementState {
    @Setter private String query = "";
    private SelectedList<ElementData> elements = SelectedList.createEmpty();
    private long requestTimestamp = System.currentTimeMillis();


    @Synchronized
    public boolean setElements(SelectedList<ElementData> elements, long requestTimestamp) {
        if (requestTimestamp <= this.requestTimestamp) {
            return false;
        }

        this.elements = elements;
        this.requestTimestamp = requestTimestamp;

        return true;
    }


    @Synchronized
    public String getSelectedId() {
        return this.elements.getSelectedItem() != null
            ? this.elements.getSelectedItem().getId()
            : null;
    }
}
