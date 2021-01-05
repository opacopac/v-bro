package com.tschanz.v_bro.data_structure.persistence.xml.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class XmlStructureData {
    @NonNull private final String elementClass;
    @NonNull private final String elementId;
    private final List<String> fwdElementIds = new ArrayList<>();
    private final List<String> bwdElementIds = new ArrayList<>(); // remark: duplicates possible
}
