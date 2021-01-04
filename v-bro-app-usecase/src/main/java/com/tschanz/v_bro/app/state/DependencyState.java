package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.Dependency;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

import java.util.Collections;
import java.util.List;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class DependencyState {
    @NonNull private List<Dependency> dependencies = Collections.emptyList();
    private boolean isFwdDependencies = true;
    @NonNull private SelectedList<ElementClass> dependencyElementClasses = SelectedList.createEmpty();
    @NonNull private MultiSelectedList<Denomination> dependencyDenominations = MultiSelectedList.createEmpty();
}
