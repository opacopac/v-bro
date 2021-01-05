package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DependencyStructureService;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlStructureData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlDependencyStructureService implements DependencyStructureService {
    private final XmlDataStructureService xmlDataStructureService;


    @Override
    public List<ElementClass> readFwdDependencies(@NonNull ElementClass elementClass) {
        var xmlStructureMap = this.xmlDataStructureService.getXmlStructureMap();
        return xmlStructureMap.values()
            .stream()
            .filter(e -> e.getElementClass().equals(elementClass.getName()))
            .flatMap(e -> e.getFwdElementIds().stream())
            .map(xmlStructureMap::get)
            .map(XmlStructureData::getElementClass)
            .distinct()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }


    @Override
    public List<ElementClass> readBwdDependencies(@NonNull ElementClass elementClass) {
        var xmlStructureMap = this.xmlDataStructureService.getXmlStructureMap();
        return xmlStructureMap.values()
            .stream()
            .filter(e -> e.getElementClass().equals(elementClass.getName()))
            .flatMap(e -> e.getBwdElementIds().stream().distinct())
            .map(xmlStructureMap::get)
            .map(XmlStructureData::getElementClass)
            .distinct()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }
}
