package com.tschanz.v_bro.structure.persistence.xml.stax_parser;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.structure.domain.model.VersionData;
import com.tschanz.v_bro.structure.persistence.xml.service.XmlRepoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class VersionParser {
    @NonNull private final XMLInputFactory xmlInputFactory;


    public List<VersionData> readVersions(InputStream xmlStream, String elementClass, String elementId) throws RepoException {
        List<VersionData> versions;

        try {
            XMLStreamReader reader = this.xmlInputFactory.createXMLStreamReader(xmlStream);
            versions = parseDocument(reader, elementClass, elementId);
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }

        return versions;
    }


    private List<VersionData> parseDocument(XMLStreamReader reader, String elementClass, String elementId) throws XMLStreamException {
        List<VersionData> versions = new ArrayList<>();

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


    private List<VersionData> parseSingleElement(XMLStreamReader reader, String elementClass, String elementId) throws XMLStreamException {
        int subLevel = 0;
        List<VersionData> versionDataList = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(XmlRepoService.VERSION_NODE_NAME)) {
                        VersionData versionData = this.parseVersion(reader);
                        if (versionData != null) {
                            versionDataList.add(versionData);
                        }
                    }
                    subLevel++;
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (subLevel < 1) {
                        if (versionDataList.size() == 0) {
                            versionDataList.add(new VersionData(elementId));
                        }
                        return versionDataList;
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private VersionData parseVersion(XMLStreamReader reader) throws XMLStreamException {
        String versionId = XmlRepoService.findId(reader);
        LocalDate gueltigVon = XmlRepoService.findDate(reader, XmlRepoService.VERSION_VON_ATTRIBUTE_NAME);
        LocalDate gueltigBis = XmlRepoService.findDate(reader, XmlRepoService.VERSION_BIS_ATTRIBUTE_NAME);

        if (versionId != null && gueltigVon != null && gueltigBis != null) {
            return new VersionData(versionId, gueltigVon, gueltigBis, Pflegestatus.PRODUKTIV);
        } else {
            return null;
        }
    }
}
