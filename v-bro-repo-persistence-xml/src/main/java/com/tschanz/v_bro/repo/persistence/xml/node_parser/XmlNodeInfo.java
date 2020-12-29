package com.tschanz.v_bro.repo.persistence.xml.node_parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class XmlNodeInfo {
    @Getter private final String name;
    @Getter private final String value;
    @Getter private final List<XmlFieldInfo> fields;
    @Getter private final List<XmlNodeInfo> childNodes;


    public boolean isValueNode() {
        return (this.fields.size() == 0 && this.childNodes.size() == 0);
    }
}
