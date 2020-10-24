package com.tschanz.v_bro.versioning.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.xml.service.XmlRepoService;
import com.tschanz.v_bro.versioning.xml.model.XmlVersionInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;


public class VersionParser {
    private final SAXParserFactory saxParserFactory;


    public VersionParser(SAXParserFactory saxParserFactory) {
        this.saxParserFactory = saxParserFactory;
    }


    public List<XmlVersionInfo> readVersions(
        XmlRepoService repoService,
        String elementName,
        String elementId
    ) throws RepoException {
        VersionParserSaxHandler handler;

        try {
            SAXParser saxParser = this.saxParserFactory.newSAXParser();
            handler = new VersionParserSaxHandler();
            handler.setElementName(elementName);
            handler.setElementId(elementId);
            saxParser.parse(repoService.getNewXmlFileStream(), handler);
        } catch (SAXException | ParserConfigurationException | IOException exception) {
            throw new RepoException(exception);
        }

        return handler.getVersionList();
    }
}
