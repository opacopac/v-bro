package com.tschanz.v_bro.elements.persistence.xml.service;

import com.tschanz.v_bro.common.xml.XmlPathTracker;
import com.tschanz.v_bro.element_classes.persistence.xml.service.ElementClassParserSaxHandler;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.model.DenominationData;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Collection;


public class ElementParserSaxHandler extends DefaultHandler {
    private final ArrayList<ElementData> elementList = new ArrayList<>();
    private String elementStructureName;
    private Collection<String> fieldNames;
    private ElementData currentElement;
    private XmlPathTracker pathTracker;


    public ArrayList<ElementData> getElementList() { return elementList; }
    public void setElementName(String elementStructureName) { this.elementStructureName = elementStructureName; }
    public void setFieldNames(Collection<String> fieldNames) { this.fieldNames = fieldNames; }


    @Override
    public void startDocument() {
        this.pathTracker = new XmlPathTracker();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        this.pathTracker.startNode(qName);

        if (this.currentElement == null && qName.equals(elementStructureName)) {
            String elementId = ElementClassParserSaxHandler.findId(attributes);

            if (elementId != null) {
                this.currentElement = new ElementData(elementId);
                this.pathTracker.setNodeMarker();
            }
        }
    }


    @Override
    public void characters(char[] ch, int start, int length) {
        if (this.isFieldNameElement()) {
            this.addFieldNameValue(new String(ch, start, length));
        }
    }


    private boolean isFieldNameElement() {
        if (this.currentElement == null) {
            return false;
        }

        if (this.pathTracker.getSubLevelFromMarker() > 2) {
            return false;
        }

        if (this.pathTracker.getSubLevelFromMarker() == 2 && !this.pathTracker.getParentNode(2).equals(XmlRepoService.VERSION_NODE_NAME)) {
            return false;
        }

        if (this.fieldNames.contains(this.pathTracker.getCurrentNode())) {
            return true;
        }

        return false;
    }


    private void addFieldNameValue(String value) {
        this.currentElement.addNameField(
            new DenominationData(
                this.pathTracker.getCurrentNode(),
                value
            )
        );
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if (this.currentElement != null && this.pathTracker.isCurrentNodeMarked()) {
            this.elementList.add(this.currentElement);
            this.currentElement = null;
        }

        this.pathTracker.endNode();
    }
}
