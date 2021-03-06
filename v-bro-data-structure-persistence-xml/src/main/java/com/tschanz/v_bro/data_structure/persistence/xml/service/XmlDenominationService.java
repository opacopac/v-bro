package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DenominationService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlFieldInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class XmlDenominationService implements DenominationService {
    private final XmlDataStructureService xmlDataStructureService;
    private final XmlNodeParser xmlNodeParser;


    @Override
    public List<Denomination> readDenominations(@NonNull ElementClass elementClass) {
        var elementNode = this.getFirstElement(elementClass.getName());
        var elementDenominations = this.getElementDenominations(elementNode);
        var versionDenominations = this.getVersionDenominations(elementNode);

        return Stream.concat(elementDenominations.stream(), versionDenominations.stream())
            .distinct()
            .collect(Collectors.toList());
    }


    // TODO: better: merge all and create set
    @SneakyThrows
    private XmlNodeInfo getFirstElement(String elementClassName) {
        var elementLutInfo = this.xmlDataStructureService.getXmlStructureMap().values()
            .stream()
            .filter(element -> elementClassName.equals(element.getElementClass()))
            .findFirst()
            .orElseThrow(() -> new RepoException(String.format("no element with name '%s' found", elementClassName)));
        var xmlFileStream = this.xmlDataStructureService.getElementInputStream(elementLutInfo.getElementId());
        this.xmlNodeParser.init(xmlFileStream, elementClassName);

        return this.xmlNodeParser.nextNode();
    }


    private List<Denomination> getElementDenominations(XmlNodeInfo elementNode) {
        return elementNode.getFields()
            .stream()
            .filter(this::isDenominationField)
            .map(f -> new Denomination(Denomination.ELEMENT_PATH, f.getName()))
            .collect(Collectors.toList());
    }


    private List<Denomination> getVersionDenominations(XmlNodeInfo elementNode) {
        return this.xmlDataStructureService.getVersionNodes(elementNode)
            .stream()
            .flatMap(node -> node.getFields().stream())
            .filter(this::isDenominationField)
            .map(f -> new Denomination(Denomination.VERSION_PATH, f.getName()))
            .collect(Collectors.toList());
    }


    private boolean isDenominationField(XmlFieldInfo field) {
        return !field.isBoolean() && !field.isDate(); // TODO more types?
    }
}
