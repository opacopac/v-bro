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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class XmlDenominationService implements DenominationService {
    private final XmlRepoService repoService;
    private final XmlNodeParser xmlNodeParser;


    @Override
    public List<Denomination> readDenominations(@NonNull ElementClass elementClass) throws RepoException {
        var elementNode = this.getFirstElement(elementClass.getName());
        var elementDenominations = this.getElementDenominations(elementNode);
        var versionDenominations = this.getVersionDenominations(elementNode);

        return Stream.concat(elementDenominations.stream(), versionDenominations.stream())
            .distinct()
            .collect(Collectors.toList());
    }


    // TODO: better: merge all and create set
    private XmlNodeInfo getFirstElement(String elementClassName) throws RepoException {
        var elementLutInfo = this.repoService.getElementLut().values()
            .stream()
            .filter(element -> elementClassName.equals(element.getName()))
            .findFirst()
            .orElseThrow(() -> new RepoException(String.format("no element with name '%s' found", elementClassName)));
        var xmlFileStream = this.repoService.getElementInputStream(elementLutInfo.getElementId());
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
        return this.repoService.getVersionNodes(elementNode)
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
