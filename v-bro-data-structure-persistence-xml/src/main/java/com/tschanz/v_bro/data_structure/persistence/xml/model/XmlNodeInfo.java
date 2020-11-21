package com.tschanz.v_bro.data_structure.persistence.xml.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class XmlNodeInfo {
    @Getter private final String name;
    @Getter private final Map<String, String> attributes = new HashMap<>();
    @Getter private final List<XmlNodeInfo> childNodes = new ArrayList<>();
    @Getter @Setter private String value = null;


    public static XmlNodeInfo parseFromSaxAttributes(String nodeName, Attributes attributes) {
        XmlNodeInfo nodeInfo = new XmlNodeInfo(nodeName);

        for (int i = 0; i < attributes.getLength(); i++) {
            nodeInfo.attributes.put(attributes.getQName(i), attributes.getValue(i));
        }

        return nodeInfo;
    }


    public boolean isValueNode() {
        return this.getAttributes().isEmpty() && this.getChildNodes().isEmpty();
    }
}
