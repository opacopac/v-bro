package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashSet;
import java.util.Set;


public class DenominationsParser {
    public static Set<Denomination> parseDenominations(XMLStreamReader reader, String elementClass) throws RepoException {
        Set<Denomination> denominations;

        try {
            denominations = parseDocument(reader, elementClass);
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }

        return denominations;
    }


    private static Set<Denomination> parseDocument(XMLStreamReader reader, String elementClass) throws XMLStreamException {
        Set<Denomination> denominations = new HashSet<>();

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

        return denominations;
    }


    private static Set<Denomination> parseElement(XMLStreamReader reader) throws XMLStreamException {
        Set<Denomination> denominations = new HashSet<>();
        int subLevel = 0;
        StringBuilder value = new StringBuilder();

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


    private static Set<Denomination> parseVersion(XMLStreamReader reader) throws XMLStreamException {
        Set<Denomination> denominations = new HashSet<>();
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
                        addDenomination(denominations, reader.getLocalName(), value.toString());
                    } else if (subLevel == 0) {
                        return denominations;
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private static void addDenomination(Set<Denomination> denominations, String name, String value) {
        if (value == null || value.trim().isEmpty()) {
            return;
        } else if (value.equals("true") || value.equals("false")) {
            return;
        } else {
            denominations.add(new Denomination(name));
        }

        // TODO: exclude some formats

    }
}
