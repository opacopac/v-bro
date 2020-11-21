package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlElementClassService implements ElementClassService {
    private final XmlRepoService repoService;
    private final DenominationsParser denominationsParser;


    @Override
    public List<ElementClass> readElementClasses() throws RepoException {
        return this.repoService.getElementLut().values()
            .stream()
            .map(XmlElementLutInfo::getName)
            .distinct()
            .sorted()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        XmlElementLutInfo elementLutInfo = this.repoService.getElementLut().values()
            .stream()
            .filter(element -> elementClass.equals(element.getName()))
            .findFirst() // TODO: better: find all and create set
            .orElseThrow(() -> new RepoException("no element with name '" + elementClass + "' found"));
        InputStream xmlFileStream = this.repoService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());

        List<Denomination> denominations = this.denominationsParser.readDenominations(xmlFileStream, elementClass);

        return denominations;
    }
}
