package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementClass;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
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
