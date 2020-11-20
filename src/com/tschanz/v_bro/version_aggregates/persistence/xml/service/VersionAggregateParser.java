package com.tschanz.v_bro.version_aggregates.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.version_aggregates.persistence.xml.model.XmlNodeInfo;
import lombok.RequiredArgsConstructor;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;


@RequiredArgsConstructor
public class VersionAggregateParser {
    private final SAXParserFactory saxParserFactory;


    public XmlNodeInfo readVersionAggregate(
        InputStream xmlInputStream,
        String elementName,
        String elementId,
        String versionId
    ) throws RepoException {
        VersionAggregateParserSaxHandler handler;

        try {
            SAXParser saxParser = this.saxParserFactory.newSAXParser();
            handler = new VersionAggregateParserSaxHandler();
            handler.setElementClass(elementName);
            handler.setElementId(elementId);
            handler.setVersionId(versionId);
            saxParser.parse(xmlInputStream, handler);
        } catch (SAXException | ParserConfigurationException | IOException exception) {
            throw new RepoException(exception);
        }

        return handler.getNodeInfo();
    }
}
