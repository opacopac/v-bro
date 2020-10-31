package com.tschanz.v_bro.element_classes.persistence.xml.model;


public class XmlElementLutInfo {
    private final String elementId;
    private final String name;
    private final int startBytePos;
    private final int endBytePos;


    public String getElementId() {
        return this.elementId;
    }
    public String getName() { return this.name; }
    public int getStartBytePos() { return this.startBytePos; }
    public int getEndBytePos() { return endBytePos; }


    public XmlElementLutInfo(String name, String elementId, int startBytePos, int endBytePos) {
        this.name = name;
        this.elementId = elementId;
        this.startBytePos = startBytePos;
        this.endBytePos = endBytePos;
    }
}
