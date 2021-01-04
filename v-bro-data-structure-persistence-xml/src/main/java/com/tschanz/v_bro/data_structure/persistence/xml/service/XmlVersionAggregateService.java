package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.common.types.KeyValue;
import com.tschanz.v_bro.data_structure.domain.model.AggregateNode;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlVersionAggregateService implements VersionAggregateService {
    private final XmlDataStructureService xmlDataStructureService;
    private final XmlNodeParser xmlNodeParser;


    @Override
    @SneakyThrows
    public VersionAggregate readVersionAggregate(@NonNull VersionData version) {
        var xmlInputStream = this.xmlDataStructureService.getElementInputStream(version.getElement().getId());
        this.xmlNodeParser.init(xmlInputStream, version.getElement().getElementClass().getName());
        var elementNode = this.xmlNodeParser.nextNode();
        var aggregateNode = this.getAggregateNode(version, elementNode);

        return new VersionAggregate(aggregateNode);
    }


    private AggregateNode getAggregateNode(VersionData version, XmlNodeInfo xmlNode) {
        return new AggregateNode(
            xmlNode.getName(),
            this.getFieldValues(xmlNode),
            xmlNode.getChildNodes()
                .stream()
                .filter(node -> this.passesVersionFilter(version, node))
                .map(node -> this.getAggregateNode(null, node))
                .collect(Collectors.toList())
        );
    }


    private boolean passesVersionFilter(VersionData version, XmlNodeInfo node) {
        if (version == null || version.isEternal()) {
            return true;
        } else if (node.getName().equals(XmlDataStructureService.VERSION_NODE_NAME)) {
            var versionId = this.xmlDataStructureService.getId(node);
            return versionId.equals(version.getId());
        } else {
            return true;
        }
    }


    private List<KeyValue> getFieldValues(XmlNodeInfo xmlNode) {
        return xmlNode.getFields()
            .stream()
            .map(field -> new KeyValue(field.getName(), field.getValue()))
            .collect(Collectors.toList());
    }
}
