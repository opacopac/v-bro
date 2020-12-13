package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class VersionAggregateState {
    private VersionAggregate versionAggregate;
}
