package com.tschanz.v_bro.repo.persistence.xml.node_parser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@EqualsAndHashCode
@RequiredArgsConstructor
public class XmlFieldInfo {
    @Getter private final boolean isAttribute;
    @Getter private final String name;
    @Getter private final String value;


    public boolean isBoolean() {
        if (this.value == null) {
            return false;
        } else {
            return this.value.equals("true") || this.value.equals("false");
        }
    }


    public boolean isDate() {
        if (this.value == null) {
            return false;
        } else {
            return this.value.matches("^\\d{4}-\\d{2}-\\d{2}$");
        }
    }
}
