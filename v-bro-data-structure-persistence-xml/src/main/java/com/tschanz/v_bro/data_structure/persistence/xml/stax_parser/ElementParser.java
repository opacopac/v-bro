package com.tschanz.v_bro.data_structure.persistence.xml.stax_parser;

import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ElementParser {
    private final XMLInputFactory xmlInputFactory;


    public List<ElementData> readElements(InputStream xmlStream, String elementClass, Collection<String> denominationFields, String query, int maxResults) throws RepoException {
        List<ElementData> elements;

        try {
            XMLStreamReader reader = this.xmlInputFactory.createXMLStreamReader(xmlStream);
            elements = parseDocument(reader, elementClass, denominationFields, query, maxResults);
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }

        return elements;
    }


    private List<ElementData> parseDocument(XMLStreamReader reader, String elementClass, Collection<String> denominationFields, String query, int maxResults) throws XMLStreamException {
        List<ElementData> elements = new ArrayList<>();

        while (reader.hasNext() && elements.size() < maxResults) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(elementClass)) {
                        String elementId = XmlRepoService.findId(reader);
                        if (elementId != null) {
                            var elementData = parseSingleElement(reader, elementClass, elementId, denominationFields, query);
                            if (elementData != null) {
                                elements.add(elementData);
                            }
                        }
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }

        return elements;
    }


    private ElementData parseSingleElement(XMLStreamReader reader, String elementClass, String elementId, Collection<String> denominationFields, String query) throws XMLStreamException {
        int subLevel = 0;
        StringBuilder value = new StringBuilder();
        List<DenominationData> denominations = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(XmlRepoService.VERSION_NODE_NAME)) {
                        denominations.addAll(parseVersion(reader, denominationFields));
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
                        if (denominationFields.contains(reader.getLocalName())) {
                            denominations.add(
                                new DenominationData(reader.getLocalName(), value.toString())
                            );
                        }
                    } else if (subLevel < 1) {
                        if (elementId.toUpperCase().contains(query.toUpperCase())
                            || denominations.stream().anyMatch(d -> d.getValue().toUpperCase().contains(query.toUpperCase()))
                        ) {
                            return new ElementData(
                                elementId,
                                denominations
                                    .stream()
                                    .distinct()
                                    .collect(Collectors.toList())
                            );
                        } else {
                            return null;
                        }
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private List<DenominationData> parseVersion(XMLStreamReader reader, Collection<String> denominationFields) throws XMLStreamException {
        List<DenominationData> denominations = new ArrayList<>();
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
                        if (denominationFields.contains(reader.getLocalName())) {
                            denominations.add(
                                new DenominationData(reader.getLocalName(), value.toString())
                            );
                        }
                    } else if (subLevel == 0) {
                        return denominations;
                    }
                    subLevel--;
                    break;
            }
        }

        throw new XMLStreamException("premature end of file");
    }
}
