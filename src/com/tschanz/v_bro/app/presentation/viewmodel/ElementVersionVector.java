package com.tschanz.v_bro.app.presentation.viewmodel;


public class ElementVersionVector {
    private final String elementClass;
    private final String elementId;
    private final String versionId;


    public String getElementClass() { return elementClass; }
    public String getElementId() { return elementId; }
    public String getVersionId() { return versionId; }


    public ElementVersionVector(
        String elementClass,
        String elementId,
        String versionId
    ) {
        this.elementClass = elementClass;
        this.elementId = elementId;
        this.versionId = versionId;
    }
}
