package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.data_structure.domain.model.VersionFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class VersionFilterState {
    private VersionFilter versionFilter = VersionFilter.DEFAULT_VERSION_FILTER;
}
