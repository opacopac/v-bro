package com.tschanz.v_bro.data_structure.persistence.xml.stax_parser;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class DenominationsParser {
    private final XMLInputFactory xmlInputFactory;


    public List<Denomination> readDenominations(InputStream xmlStream, String elementClass) throws RepoException {
        List<Denomination> denominations;

        try {
            XMLStreamReader reader = this.xmlInputFactory.createXMLStreamReader(xmlStream);
            denominations = parseDocument(reader, elementClass);
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }

        return denominations;
    }


    private List<Denomination> parseDocument(XMLStreamReader reader, String elementClass) throws XMLStreamException {
        List<Denomination> denominations = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(elementClass)) {
                        denominations.addAll(parseElement(reader));
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }

        return denominations
            .stream()
            .distinct()
            .collect(Collectors.toList());
    }


    private List<Denomination> parseElement(XMLStreamReader reader) throws XMLStreamException {
        List<Denomination> elementDenominations = new ArrayList<>();
        List<Denomination> versionDenominations = new ArrayList<>();
        int subLevel = 0;
        StringBuilder value = new StringBuilder();

        this.addAttributesDenominations(elementDenominations, reader, Denomination.ELEMENT_PATH);

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(XmlRepoService.VERSION_NODE_NAME)) {
                        versionDenominations.addAll(parseVersion(reader));
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
                        addDenomination(elementDenominations, Denomination.ELEMENT_PATH, reader.getLocalName(), value.toString()); // TODO => use name of container node
                    } else if (subLevel < 1) {
                        return Stream.concat(elementDenominations.stream(), versionDenominations.stream()).collect(Collectors.toList());
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private List<Denomination> parseVersion(XMLStreamReader reader) throws XMLStreamException {
        List<Denomination> denominations = new ArrayList<>();
        int subLevel = 0;
        StringBuilder value = new StringBuilder();

        this.addAttributesDenominations(denominations, reader, Denomination.VERSION_PATH);

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    subLevel++;
                    if (subLevel == 1) {
                        value.setLength(0); // clear current value
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (subLevel == 1) {
                        value.append(reader.getText());
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (subLevel == 1) {
                        addDenomination(denominations, Denomination.VERSION_PATH, reader.getLocalName(), value.toString()); // TODO => use name of container node
                    } else if (subLevel == 0) {
                        return denominations;
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private void addAttributesDenominations(List<Denomination> denominations, XMLStreamReader reader, String path) {
        for (var i = 0; i < reader.getAttributeCount(); i++) {
            this.addDenomination(denominations, path, reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        }
    }


    private void addDenomination(List<Denomination> denominations, String path, String name, String value) {
        if (value == null || value.trim().isEmpty()) {
            return;
        } else if (value.equals("true") || value.equals("false")) {
            return;
        } else if (value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return;
        } else if (!denominations.contains(new Denomination(path, name))) {
            denominations.add(new Denomination(path, name));
        }

        // TODO: exclude some other formats
    }
}
