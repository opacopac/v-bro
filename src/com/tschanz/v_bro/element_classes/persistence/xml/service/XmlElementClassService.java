package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


public class XmlElementClassService implements ElementClassService {
    private final XmlRepoService repoService;
    private final XMLInputFactory xmlInputFactory;
    private final List<XmlElementLutInfo> elements = new ArrayList<>();
    private Map<String, XmlElementLutInfo> elementStructureMap;


    public XmlElementClassService(
        XmlRepoService repoService,
        XMLInputFactory xmlInputFactory
    ) {
        this.repoService = repoService;
        this.xmlInputFactory = xmlInputFactory;
    }


    public Map<String, XmlElementLutInfo> getElementLut() throws RepoException {
        if (this.elementStructureMap == null) {
            this.readElementLut();
        }

        return this.elementStructureMap;
    }


    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        if (!this.repoService.isConnected()) {
            throw new RepoException("Repo not connected");
        }

        return this.getElementLut().values()
            .stream()
            .map(XmlElementLutInfo::getName)
            .distinct()
            .sorted()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        XmlElementLutInfo elementLutInfo = this.getElementLut().values()
            .stream()
            .filter(element -> elementClass.equals(element.getName()))
            .findFirst() // TODO: better: find all and create set
            .orElseThrow(() -> new RepoException("no element with name '" + elementClass + "' found"));
        InputStream xmlFileStream = this.repoService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
        XMLStreamReader reader = this.getReader(xmlFileStream);

        Set<Denomination> denominations = DenominationsParser.parseDenominations(reader, elementClass);

        return new ArrayList<>(denominations);
    }


    private void readElementLut() throws RepoException {
        InputStream xmlFileStream = this.repoService.getNewXmlFileStream();
        ElementLutParser parser = new ElementLutParser();
        List<XmlElementLutInfo> elements = parser.readElementLut(xmlFileStream);

        this.elementStructureMap = new HashMap<>();
        elements.forEach(element -> this.elementStructureMap.put(element.getElementId(), element));
    }


    private XMLStreamReader getReader(InputStream xmlFileStream) throws RepoException {
        try {
            return this.xmlInputFactory.createXMLStreamReader(xmlFileStream);
        } catch (XMLStreamException exception) {
            throw new RepoException(exception.getMessage(), exception);
        }
    }
}
