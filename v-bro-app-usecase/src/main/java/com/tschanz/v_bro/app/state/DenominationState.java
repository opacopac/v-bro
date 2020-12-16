package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

import java.util.List;
import java.util.stream.Collectors;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class DenominationState {
    @NonNull private MultiSelectedList<Denomination> denominations = MultiSelectedList.createEmpty();


    public List<String> getSelectedNames() {
        return this.denominations.getSelectedItems()
            .stream()
            .map(Denomination::getName)
            .collect(Collectors.toList());
    }
}
