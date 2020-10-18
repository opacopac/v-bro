package com.tschanz.v_bro.elements.xml.service;

import com.tschanz.v_bro.elements.xml.model.XmlElementClass;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.xml.service.XmlRepoService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;


public class ElementClassParser {
    private final SAXParserFactory saxParserFactory;


    public ElementClassParser(SAXParserFactory saxParserFactory) {
        this.saxParserFactory = saxParserFactory;
    }


    public Collection<XmlElementClass> readElementStructure(XmlRepoService repoService) throws RepoException {
        ElementClassParserSaxHandler handler;

        try {
            SAXParser saxParser = this.saxParserFactory.newSAXParser();
            handler = new ElementClassParserSaxHandler();
            saxParser.parse(repoService.getNewXmlFileStream(), handler);
        } catch (SAXException | ParserConfigurationException | IOException exception) {
            throw new RepoException(exception);
        }

        return handler.getElementStructureMap().values();
    }
}
