package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class ElementClassState {
    @NonNull private SelectedList<ElementClass> elementClasses = SelectedList.createEmpty();


    public String getSelectedName() {
        return this.elementClasses.getSelectedItem() != null
            ? this.elementClasses.getSelectedItem().getName()
            : null;
    }
}
