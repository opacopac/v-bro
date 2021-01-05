package com.tschanz.v_bro.repo.persistence.xml.idref_parser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@EqualsAndHashCode
@RequiredArgsConstructor
public class XmlIdElementPosInfo {
    @Getter private final String name;
    @Getter private final String elementId;
    @Getter private final int startBytePos;
    @Getter private final int endBytePos;
    @Getter private final List<String> idRefs;
}
