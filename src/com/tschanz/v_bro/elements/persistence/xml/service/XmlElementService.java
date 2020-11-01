package com.tschanz.v_bro.elements.persistence.xml.service;

import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


public class XmlElementService implements ElementService {
    private final XmlRepoService repoService;
    private final ElementParser elementParser;


    public XmlElementService(
        XmlRepoService repoService,
        ElementParser elementParser
    ) {
        this.repoService = repoService;
        this.elementParser = elementParser;
    }


    @Override
    public Collection<ElementData> readElements(String elementClass, Collection<String> denominationFields) throws RepoException {
        List<XmlElementLutInfo> elementLuts = this.repoService.getElementLut().values()
            .stream()
            .filter(element -> elementClass.equals(element.getName()))
            .sorted(Comparator.comparingInt(XmlElementLutInfo::getStartBytePos))
            .collect(Collectors.toList());
        int minBytePos = elementLuts.get(0).getStartBytePos();
        int maxBytePos = elementLuts.get(elementLuts.size() - 1).getEndBytePos();

        InputStream xmlFileStream = this.repoService.getNewXmlFileStream(minBytePos, maxBytePos);

        Collection<ElementData> elementDataList = this.elementParser.readElements(
            xmlFileStream,
            elementClass,
            denominationFields
        );

        return new ArrayList<>(elementDataList);
    }
}
