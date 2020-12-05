package com.tschanz.v_bro.app.usecase.common.responsemodel;

import com.tschanz.v_bro.common.KeyValue;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class VersionAggregateNodeResponse {
    public final String nodeName;
    public final List<KeyValue> fieldValues;
    public final List<VersionAggregateNodeResponse> childNodes;
}
