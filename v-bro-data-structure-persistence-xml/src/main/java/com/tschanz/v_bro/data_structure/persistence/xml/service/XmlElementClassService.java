package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlStructureData;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlElementClassService implements ElementClassService {
    private final XmlDataStructureService xmlDataStructureService;


    @Override
    public List<ElementClass> readAllElementClasses() {
        return this.xmlDataStructureService.getXmlStructureMap().values()
            .stream()
            .map(XmlStructureData::getElementClass)
            .distinct()
            .sorted()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }
}
