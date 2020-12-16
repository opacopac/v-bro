package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.data_structure.domain.model.FwdDependency;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

import java.util.Collections;
import java.util.List;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class DependencyState {
    @NonNull private List<FwdDependency> fwdDependencies = Collections.emptyList();
}
