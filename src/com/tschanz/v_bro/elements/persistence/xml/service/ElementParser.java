package com.tschanz.v_bro.elements.persistence.xml.service;

import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.List;


public class ElementParser {
    private final SAXParserFactory saxParserFactory;


    public ElementParser(SAXParserFactory saxParserFactory) {
        this.saxParserFactory = saxParserFactory;
    }


    public List<ElementData> readElementList(
        XmlRepoService repoService,
        String elementName,
        Collection<String> fieldNames
    ) throws RepoException {
        ElementParserSaxHandler handler;

        try {
            SAXParser saxParser = this.saxParserFactory.newSAXParser();
            handler = new ElementParserSaxHandler();
            handler.setElementName(elementName);
            handler.setFieldNames(fieldNames);
            saxParser.parse(repoService.getNewXmlFileStream(), handler);
        } catch (SAXException | ParserConfigurationException | IOException exception) {
            throw new RepoException(exception);
        }

        return handler.getElementList();
    }
}
