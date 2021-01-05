package com.tschanz.v_bro.repo.persistence.xml.idref_parser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@EqualsAndHashCode
@RequiredArgsConstructor
public class XmlIdRefPosInfo {
    @Getter private final String idRef;
    @Getter private final int startBytePos;
}
