package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.common.KeyValue;
import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlNodeInfo;
import com.tschanz.v_bro.data_structure.persistence.xml.sax_parser.VersionAggregateParser;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class XmlVersionAggregateService implements VersionAggregateService {
    private final XmlRepoService repoService;
    private final VersionAggregateParser parser;


    @Override
    public VersionAggregate readVersionAggregate(@NonNull VersionData version) throws RepoException {
        XmlElementLutInfo elementLutInfo = this.repoService.getElementLut().get(version.getElement().getId());
        InputStream xmlInputStream = this.repoService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
        XmlNodeInfo rootNode = this.parser.readVersionAggregate(xmlInputStream, version.getElement().getElementClass().getName(), version.getElement().getId(), version.getId());

        return new VersionAggregate(
            this.getVersionInfo(rootNode, version.getElement()),
            this.getAggregateNode(rootNode)
        );
    }


    private VersionData getVersionInfo(XmlNodeInfo nodeInfo, ElementData element) {
        if (nodeInfo.getChildNodes().isEmpty()) {
            return VersionData.createEternal(element);
        }

        XmlNodeInfo versionNode = nodeInfo.getChildNodes().get(0);
        String id = versionNode.getAttributes().get(XmlRepoService.ID_ATTRIBUTE_NAME);
        String gueltigVon = versionNode.getAttributes().get(XmlRepoService.VERSION_VON_ATTRIBUTE_NAME);
        String gueltigBis = versionNode.getAttributes().get(XmlRepoService.VERSION_BIS_ATTRIBUTE_NAME);

        if (id == null || gueltigVon == null || gueltigBis == null) {
            return VersionData.createEternal(element);
        }

        return new VersionData(
            element,
            id,
            LocalDate.parse(gueltigVon),
            LocalDate.parse(gueltigBis),
            Pflegestatus.PRODUKTIV
        );
    }


    private AggregateNode getAggregateNode(XmlNodeInfo xmlNode) {
        return new AggregateNode(
            xmlNode.getName(),
            this.getFieldValues(xmlNode),
            xmlNode.getChildNodes()
                .stream()
                .filter(node -> !node.isValueNode())
                .map(this::getAggregateNode)
                .collect(Collectors.toList())
        );
    }


    private List<KeyValue> getFieldValues(XmlNodeInfo xmlNode) {
        return Stream.concat(
            xmlNode.getAttributes().keySet()
                .stream()
                .map(key -> new KeyValue(key, xmlNode.getAttributes().get(key))),
            xmlNode.getChildNodes()
                .stream()
                .filter(XmlNodeInfo::isValueNode)
                .map(node -> new KeyValue(node.getName(), node.getValue()))
        ).collect(Collectors.toList());
    }
}
