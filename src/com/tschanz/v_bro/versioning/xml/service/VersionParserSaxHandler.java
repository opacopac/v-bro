package com.tschanz.v_bro.versioning.xml.service;

import com.tschanz.v_bro.elements.xml.service.ElementClassParserSaxHandler;
import com.tschanz.v_bro.elements.xml.service.XmlPathTracker;
import com.tschanz.v_bro.repo.xml.service.XmlRepoService;
import com.tschanz.v_bro.versioning.xml.model.XmlVersionInfo;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class VersionParserSaxHandler extends DefaultHandler {
    private final ArrayList<XmlVersionInfo> versionList = new ArrayList<>();
    private String elementName;
    private String elementId;
    private int elementLevel = -1;
    private final Collection<String> elementFwdDepIds = new ArrayList<>();
    private XmlVersionInfo currentVersion;
    private XmlPathTracker pathTracker;


    public ArrayList<XmlVersionInfo> getVersionList() { return versionList; }

    public void setElementName(String elementName) { this.elementName = elementName; }

    public void setElementId(String elementId) { this.elementId = elementId; }


    @Override
    public void startDocument() {
        this.pathTracker = new XmlPathTracker();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        this.pathTracker.startNode(qName);

        if (this.elementLevel == -2) {
            return;
        }

        if (this.elementLevel == -1 && qName.equals(elementName)) {
            String elementId = ElementClassParserSaxHandler.findId(attributes);
            if (elementId != null && elementId.equals(this.elementId)) {
                this.elementLevel = this.pathTracker.getCurrentPath().size();
                this.elementFwdDepIds.clear();
            }
            return;
        }

        if (this.pathTracker.getCurrentPath().size() == this.elementLevel + 1 && qName.equals(XmlRepoService.VERSION_ELEMENT_NAME)) {
            String versionId = ElementClassParserSaxHandler.findId(attributes);
            LocalDate gueltigVon = this.findDate(attributes, XmlRepoService.VERSION_VON_ATTRIBUTE_NAME);
            LocalDate gueltigBis = this.findDate(attributes, XmlRepoService.VERSION_BIS_ATTRIBUTE_NAME);

            if (versionId != null && gueltigVon != null && gueltigBis != null) {
                this.currentVersion = new XmlVersionInfo(versionId, gueltigVon, gueltigBis);
                this.pathTracker.setNodeMarker();
            }
        }
    }


    private LocalDate findDate(Attributes attributes, String attributeName) {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getQName(i).equals(attributeName)) {
                String dateString = attributes.getValue(i);
                return LocalDate.parse(dateString);
            }
        }

        return null;
    }


    @Override
    public void characters(char[] ch, int start, int length) {
        if (this.elementLevel >= 0) {
            String fwdIdRefsText =  new String(ch, start, length);
            Collection<String> fwdIdRefs = this.findDependencyIds(fwdIdRefsText);
            if (fwdIdRefs != null) {
                if (this.currentVersion != null) {
                    this.currentVersion.addFwdDepIds(fwdIdRefs);
                } else {
                    this.elementFwdDepIds.addAll(fwdIdRefs);
                }
            }
        }
    }


    private Collection<String> findDependencyIds(String value) {
        if (value.startsWith(XmlRepoService.ID_VALUE_PREFIX_1) || value.startsWith(XmlRepoService.ID_VALUE_PREFIX_2)) {
            return Arrays.asList(value.split(" "));
        } else {
            return null;
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if (this.elementLevel >= 0 && this.pathTracker.isCurrentNodeMarked()) {
            this.versionList.add(this.currentVersion);
            this.currentVersion = null;
            this.pathTracker.removeNodeMarker();
        }

        if (this.elementLevel >= this.pathTracker.getCurrentPath().size()) {
            if (this.versionList.size() == 0) {
                XmlVersionInfo unversionedEntry = new XmlVersionInfo(this.elementId);
                unversionedEntry.addFwdDepIds(this.elementFwdDepIds);
                this.versionList.add(unversionedEntry);
            }
            this.elementLevel = -2;
        }

        this.pathTracker.endNode();
    }
}
