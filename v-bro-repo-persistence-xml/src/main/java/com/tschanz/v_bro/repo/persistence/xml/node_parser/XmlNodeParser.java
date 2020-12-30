package com.tschanz.v_bro.repo.persistence.xml.node_parser;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class XmlNodeParser {
    private final XMLInputFactory xmlInputFactory;
    private XMLStreamReader reader;
    private String xmlElementName;


    public void init(@NonNull InputStream xmlStream, @NonNull String xmlElementName) throws RepoException {
        try {
            this.reader = this.xmlInputFactory.createXMLStreamReader(xmlStream);
            this.xmlElementName = xmlElementName;
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }
    }


    public XmlNodeInfo nextNode() throws RepoException {
        if (this.reader == null) {
            throw new IllegalArgumentException("reader has not been initialized");
        }

        try {
            while (reader.hasNext()) {
                if (reader.next() == XMLStreamReader.START_ELEMENT) {
                    if (reader.getLocalName().equals(this.xmlElementName)) {
                        return this.parseXmlElement();
                    }
                }
            }

            this.reader.close();
        } catch (XMLStreamException exception) {
            throw new RepoException(exception);
        }

        return null;
    }


    private XmlNodeInfo parseXmlElement() throws XMLStreamException {
        StringBuilder value = new StringBuilder();
        List<XmlNodeInfo> childNodes = new ArrayList<>();
        List<XmlFieldInfo> fields = this.parseAttributeFields();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    var childNode = this.parseXmlElement();
                    if (childNode.isValueNode()) {
                        var field = new XmlFieldInfo(false, childNode.getName(), childNode.getValue());
                        fields.add(field);
                    } else {
                        childNodes.add(childNode);
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    value.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return new XmlNodeInfo(reader.getLocalName(), value.toString(), fields, childNodes);
            }
        }

        throw new XMLStreamException("premature end of file");
    }


    private List<XmlFieldInfo> parseAttributeFields() {
        List<XmlFieldInfo> fields = new ArrayList<>();

        for (var i = 0; i < this.reader.getAttributeCount(); i++) {
            var field = new XmlFieldInfo(true, reader.getAttributeLocalName(i), reader.getAttributeValue(i));
            fields.add(field);
        }

        return fields;
    }
}
