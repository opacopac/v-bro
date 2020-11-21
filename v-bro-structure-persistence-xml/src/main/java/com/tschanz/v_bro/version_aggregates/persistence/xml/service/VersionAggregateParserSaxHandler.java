package com.tschanz.v_bro.version_aggregates.persistence.xml.service;

import com.tschanz.v_bro.common.xml.XmlPathTracker2;
import com.tschanz.v_bro.element_classes.persistence.xml.service.ElementClassParserSaxHandler;
import com.tschanz.v_bro.element_classes.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.version_aggregates.persistence.xml.model.XmlNodeInfo;
import lombok.Setter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;


public class VersionAggregateParserSaxHandler extends DefaultHandler {
    private final Stack<XmlNodeInfo> nodeStack = new Stack<>();
    @Setter private String elementClass;
    @Setter private String elementId;
    @Setter private String versionId;
    private XmlPathTracker2 pathTracker;


    public XmlNodeInfo getNodeInfo() {
        return nodeStack.peek();
    }


    @Override
    public void startDocument() {
        this.pathTracker = new XmlPathTracker2();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        this.pathTracker.startNode(qName);

        if (this.isElementStart(qName, attributes)) {
            this.pathTracker.markNode("element");
            this.addNode(qName, attributes);
            if (this.versionId.equals(this.elementId)) { // TODO: or null?
                this.pathTracker.markNode("version");
            }
        } else if (this.isVersionStart(qName, attributes)) {
            this.pathTracker.markNode("version");
        }

        if (this.pathTracker.isWithinMarker("version")) {
            this.addNode(qName, attributes);
        }
    }


    private boolean isElementStart(String qName, Attributes attributes) {
        return qName.equals(elementClass)
            && !this.pathTracker.isWithinMarker("element")
            && this.elementId.equals(ElementClassParserSaxHandler.findId(attributes));
    }


    private boolean isVersionStart(String qName, Attributes attributes) {
        return qName.equals(XmlRepoService.VERSION_NODE_NAME)
            && !this.pathTracker.isWithinMarker("version")
            && this.pathTracker.getSubLevelOfMarker("element") == 1
            && this.versionId.equals(ElementClassParserSaxHandler.findId(attributes));
    }


    private void addNode(String qName, Attributes attributes) {
        XmlNodeInfo xmlNode = XmlNodeInfo.parseFromSaxAttributes(qName, attributes);
        this.nodeStack.push(xmlNode);
    }


    @Override
    public void characters(char[] ch, int start, int length) {
        if (this.nodeStack.size() > 1) {
            this.nodeStack.peek().setValue(new String(ch, start, length));
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if (this.nodeStack.size() > 1) {
            XmlNodeInfo childNode = this.nodeStack.pop();
            XmlNodeInfo parentNode = this.nodeStack.peek();
            parentNode.getChildNodes().add(childNode);
        }

        this.pathTracker.endNode();
    }
}
