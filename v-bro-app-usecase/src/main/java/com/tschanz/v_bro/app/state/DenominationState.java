package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class DenominationState {
    @NonNull private MultiSelectedList<Denomination> elementDenominations = MultiSelectedList.createEmpty();
    private Map<String, List<Denomination>> lastSelectedDenominations = new HashMap<>();
}
