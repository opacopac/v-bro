package com.tschanz.v_bro.version_aggregates.persistence.xml.model;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XmlNodeInfo {
    private final String name;
    private final Map<String, String> attributes = new HashMap<>();
    private final List<XmlNodeInfo> childNodes = new ArrayList<>();
    private String value = null;


    public String getName() { return name; }
    public Map<String, String> getAttributes() { return attributes; }
    public List<XmlNodeInfo> getChildNodes() { return childNodes; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }


    public XmlNodeInfo(String name) {
        this.name = name;
    }


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
