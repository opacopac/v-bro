package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class XmlElementService implements ElementService {
    private final XmlRepoService repoService;
    private final XmlNodeParser xmlNodeParser;


    @Override
    public List<ElementData> readElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    ) throws RepoException {
        var xmlFileStream = this.repoService.getElementClassInputStream(elementClass.getName());
        this.xmlNodeParser.init(xmlFileStream, elementClass.getName());

        List<ElementData> elements = new ArrayList<>();
        while (elements.size() < maxResults) {
            var elementNode = this.xmlNodeParser.nextNode();
            if (elementNode != null) {
                var versionNodes = this.repoService.getVersionNodes(elementNode);
                if (this.isQueryMatch(elementNode, versionNodes, denominationFields, query)) {
                    var element = this.createElement(elementClass, elementNode, versionNodes, denominationFields);
                    elements.add(element);
                }
            } else {
                break;
            }
        }

        return elements;
    }


    private boolean isQueryMatch(XmlNodeInfo elementNode, List<XmlNodeInfo> versionNodes, List<Denomination> denominationFields, String query) {
        if (this.isQueryMatch(elementNode, denominationFields, Denomination.ELEMENT_PATH, query)) {
            return true;
        } else {
            for (var versionNode: versionNodes) {
                if (this.isQueryMatch(versionNode, denominationFields, Denomination.VERSION_PATH, query)) {
                    return true;
                }
            }
        }

        return false;
    }


    private boolean isQueryMatch(XmlNodeInfo node, List<Denomination> denominationFields, String path, String query) {
        var denominationFieldNames = denominationFields.stream().filter(d -> d.getPath().equals(path)).map(Denomination::getName).collect(Collectors.toList());

        return node.getFields()
            .stream()
            .filter(f -> denominationFieldNames.contains(f.getName()))
            .filter(f -> f.getValue().toLowerCase().contains(query.toLowerCase()))
            .map(f -> true)
            .findFirst()
            .orElse(false);
    }


    private ElementData createElement(ElementClass elementClass, XmlNodeInfo elementNode, List<XmlNodeInfo> versionNodes, List<Denomination> denominationFields) {
        var elementId = this.repoService.getId(elementNode);
        List<DenominationData> denominations = new ArrayList<>(this.createDenominations(elementNode, denominationFields, Denomination.ELEMENT_PATH));
        if (versionNodes.size() > 0) {
            var latestVersion = versionNodes.get(versionNodes.size() - 1);
            denominations.addAll(this.createDenominations(latestVersion, denominationFields, Denomination.VERSION_PATH));
        }

        return new ElementData(elementClass, elementId, denominations);
    }


    private List<DenominationData> createDenominations(XmlNodeInfo node, List<Denomination> denominationFields, String path) {
        if (node == null) {
            return Collections.emptyList();
        }

        var denominationFieldNames = denominationFields.stream().filter(d -> d.getPath().equals(path)).map(Denomination::getName).collect(Collectors.toList());

        return node.getFields()
            .stream()
            .filter(f -> denominationFieldNames.contains(f.getName()))
            .map(f -> new DenominationData(f.getName(), f.getValue()))
            .collect(Collectors.toList());
    }
}
