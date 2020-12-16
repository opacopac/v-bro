package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class VersionState {
    @NonNull private SelectedList<VersionData> versions = SelectedList.createEmpty();


    public String getSelectedVersionId() {
        return this.versions.getSelectedItem() != null
            ? this.versions.getSelectedItem().getId()
            : null;
    }
}
