package com.tschanz.v_bro.data_structure.persistence.xml.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class XmlElementLutInfo {
    @Getter private final String name;
    @Getter private final String elementId;
    @Getter private final int startBytePos;
    @Getter private final int endBytePos;
}
