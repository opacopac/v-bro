package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.domain.model.Denomination;
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
                        checkAddDenomination(elementDenominations, reader.getLocalName(), value.toString());
                    } else if (subLevel < 1) {
                        return Stream.concat(
                            elementDenominations.stream(),
                            versionDenominations.stream()
                        )
                            .collect(Collectors.toList());
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
                        checkAddDenomination(denominations, reader.getLocalName(), value.toString());
                    } else if (subLevel == 0) {
                        return denominations;
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private void checkAddDenomination(List<Denomination> denominations, String name, String value) {
        if (value == null || value.trim().isEmpty()) {
            return;
        } else if (value.equals("true") || value.equals("false")) {
            return;
        } else if (!denominations.contains(name)) {
            denominations.add(new Denomination(name));
        }

        // TODO: exclude some other formats
    }
}
