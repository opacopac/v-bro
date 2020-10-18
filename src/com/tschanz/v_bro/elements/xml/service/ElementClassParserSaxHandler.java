package com.tschanz.v_bro.elements.xml.service;

import com.tschanz.v_bro.elements.xml.model.XmlElementStructurePart;
import com.tschanz.v_bro.elements.xml.model.XmlElementClass;
import com.tschanz.v_bro.repo.xml.service.XmlRepoService;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;


public class ElementClassParserSaxHandler extends DefaultHandler {
    private final HashMap<String, XmlElementClass> elementStructureMap = new HashMap<>();
    private XmlElementStructurePart currentElement;
    private XmlPathTracker pathTracker;


    public HashMap<String, XmlElementClass> getElementStructureMap() { return elementStructureMap; }


    @Override
    public void startDocument() {
        this.pathTracker = new XmlPathTracker();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        this.pathTracker.startNode(qName);

        if (this.currentElement == null) {
            this.detectAndSetCurrentElement(qName, attributes);
        }
    }


    private void detectAndSetCurrentElement(String elementStructureName, Attributes attributes) {
        String id = findId(attributes);
        if (id != null) {
            this.setCurrentElement(elementStructureName, id);
        }
    }


    public static String findId(Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getQName(i).equals(XmlRepoService.ID_ATTRIBUTE_NAME)) {
                String value = attributes.getValue(i);
                if (value.startsWith(XmlRepoService.ID_VALUE_PREFIX_1) || value.startsWith(XmlRepoService.ID_VALUE_PREFIX_2)) {
                    return value;
                }
            }
        }

        return null;
    }


    private void setCurrentElement(String elementStructureName, String elementId) {
        this.currentElement = new XmlElementStructurePart(elementStructureName, elementId);
        this.pathTracker.setNodeMarker();
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.isPotentialFieldName()) {
            String value = new String(ch, start, length);

            if (this.isFieldNameDataFormat(value)) {
                this.currentElement.addFieldName(this.pathTracker.getCurrentNode());
            }
        }
    }


    public boolean isPotentialFieldName() {
        if (this.currentElement == null) {
            return false;
        }

        if (this.pathTracker.getSubLevelFromMarker() == 1 && !this.pathTracker.getCurrentNode().equals(XmlRepoService.VERSION_ELEMENT_NAME)) {
            return true;
        }

        if (this.pathTracker.getSubLevelFromMarker() == 2 && this.pathTracker.getParentNode(2).equals(XmlRepoService.VERSION_ELEMENT_NAME)) {
            return true;
        }

        return false;
    }


    private boolean isFieldNameDataFormat(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        if (value.equals("true") || value.equals("false")) {
            return false;
        }

        // TODO: exclude some formats

        return true;
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if (this.isCurrentElementData(qName)) {
            this.mergeElementPart(qName);
            this.resetCurrentElement();
        }

        this.pathTracker.endNode();
    }


    private boolean isCurrentElementData(String elementStructureName) {
        return (this.currentElement != null && this.currentElement.getName().equals(elementStructureName));
    }


    private void mergeElementPart(String elementStructureName) {
        XmlElementClass structure;
        if (!this.elementStructureMap.containsKey(elementStructureName)) {
            structure = new XmlElementClass(elementStructureName);
            this.elementStructureMap.put(elementStructureName, structure);
        } else {
            structure = this.elementStructureMap.get(elementStructureName);
        }
        structure.addElementData(this.currentElement);
    }


    private void resetCurrentElement() {
        this.currentElement = null;
        this.pathTracker.removeNodeMarker();
    }
}
