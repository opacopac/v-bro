package com.tschanz.v_bro.data_structure.domain.model;

import com.tschanz.v_bro.common.types.KeyValue;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class AggregateNode {
    @Getter @NonNull protected final String nodeName;
    @Getter @NonNull protected final List<KeyValue> fieldValues;
    @Getter @NonNull protected final List<AggregateNode> childNodes;
}

