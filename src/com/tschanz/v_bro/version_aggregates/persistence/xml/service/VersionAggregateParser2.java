package com.tschanz.v_bro.version_aggregates.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.version_aggregates.persistence.xml.model.XmlNodeInfo;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class VersionAggregateParser2 {
    private final XMLInputFactory xmlInputFactory;


    public VersionAggregateParser2(XMLInputFactory xmlInputFactory) {
        this.xmlInputFactory = xmlInputFactory;
    }


/*    public XmlNodeInfo readVersionAggregate(InputStream xmlStream, String elementClass, String elementId, String versionId) throws RepoException {
        List<VersionInfo> versions;

        try {
            XMLStreamReader reader = this.xmlInputFactory.createXMLStreamReader(xmlStream);
            versions = parseDocument(reader, elementClass, elementId);
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }

        return versions;
    }


    private XmlNodeInfo parseDocument(XMLStreamReader reader, String elementClass, String elementId, String versionId) throws XMLStreamException {
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(elementClass)) {
                        String currentElementId = XmlRepoService.findId(reader);
                        if (elementId.equals(currentElementId)) {
                            return parseElement(reader, elementClass, elementId, versionId);
                        }
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private XmlNodeInfo parseElement(XMLStreamReader reader, String elementClass, String elementId, String versionId) throws XMLStreamException {
        int subLevel = 0;
        List<VersionInfo> versionInfos = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(XmlRepoService.VERSION_NODE_NAME)) {
                        denominations.addAll(parseVersion(reader));
                    } else {
                        subLevel++;
                        if (subLevel == 1) {
                            value.setLength(0); // clear current value
                        }
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (subLevel == 1) {
                        value.append(reader.getText());
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (subLevel == 1) {
                        addDenomination(denominations, reader.getLocalName(), value.toString());
                    } else if (subLevel < 1) {
                        return denominations;
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private XmlNodeInfo parseVersion(XMLStreamReader reader) throws XMLStreamException {
        String versionId = XmlRepoService.findId(reader);
        LocalDate gueltigVon = XmlRepoService.findDate(reader, XmlRepoService.VERSION_VON_ATTRIBUTE_NAME);
        LocalDate gueltigBis = XmlRepoService.findDate(reader, XmlRepoService.VERSION_BIS_ATTRIBUTE_NAME);

        if (versionId != null && gueltigVon != null && gueltigBis != null) {
            return new VersionInfo(versionId, gueltigVon, gueltigBis, Pflegestatus.PRODUKTIV);
        } else {
            return null;
        }
    }*/
}
