package com.tschanz.v_bro.versions.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class VersionParser {
    private final XMLInputFactory xmlInputFactory;


    public VersionParser(XMLInputFactory xmlInputFactory) {
        this.xmlInputFactory = xmlInputFactory;
    }


    public List<VersionInfo> readVersions(InputStream xmlStream, String elementClass, String elementId) throws RepoException {
        List<VersionInfo> versions;

        try {
            XMLStreamReader reader = this.xmlInputFactory.createXMLStreamReader(xmlStream);
            versions = parseDocument(reader, elementClass, elementId);
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }

        return versions;
    }


    private List<VersionInfo> parseDocument(XMLStreamReader reader, String elementClass, String elementId) throws XMLStreamException {
        List<VersionInfo> versions = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(elementClass)) {
                        String currentElementId = XmlRepoService.findId(reader);
                        if (elementId.equals(currentElementId)) {
                            versions.addAll(parseSingleElement(reader, elementClass, elementId));
                        }
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }

        return versions;
    }


    private List<VersionInfo> parseSingleElement(XMLStreamReader reader, String elementClass, String elementId) throws XMLStreamException {
        int subLevel = 0;
        List<VersionInfo> versionInfos = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(XmlRepoService.VERSION_NODE_NAME)) {
                        VersionInfo versionInfo = this.parseVersion(reader);
                        if (versionInfo != null) {
                            versionInfos.add(versionInfo);
                        }
                    }
                    subLevel++;
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (subLevel < 1) {
                        if (versionInfos.size() == 0) {
                            versionInfos.add(new VersionInfo(elementId));
                        }
                        return versionInfos;
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private VersionInfo parseVersion(XMLStreamReader reader) throws XMLStreamException {
        String versionId = XmlRepoService.findId(reader);
        LocalDate gueltigVon = XmlRepoService.findDate(reader, XmlRepoService.VERSION_VON_ATTRIBUTE_NAME);
        LocalDate gueltigBis = XmlRepoService.findDate(reader, XmlRepoService.VERSION_BIS_ATTRIBUTE_NAME);

        if (versionId != null && gueltigVon != null && gueltigBis != null) {
            return new VersionInfo(versionId, gueltigVon, gueltigBis, Pflegestatus.PRODUKTIV);
        } else {
            return null;
        }
    }
}
