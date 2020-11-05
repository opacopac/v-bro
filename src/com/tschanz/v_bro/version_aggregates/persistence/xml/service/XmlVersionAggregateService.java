package com.tschanz.v_bro.version_aggregates.persistence.xml.service;

import com.tschanz.v_bro.common.KeyValue;
import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.version_aggregates.domain.model.AggregateNode;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.xml.model.XmlNodeInfo;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionData;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class XmlVersionAggregateService implements VersionAggregateService {
    private final XmlRepoService repoService;
    private final VersionAggregateParser parser;


    public XmlVersionAggregateService(
        XmlRepoService repoService,
        VersionAggregateParser parser
    ) {
        this.repoService = repoService;
        this.parser = parser;
    }


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        XmlElementLutInfo elementLutInfo = this.repoService.getElementLut().get(elementId);
        InputStream xmlInputStream = this.repoService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
        XmlNodeInfo rootNode = this.parser.readVersionAggregate(xmlInputStream, elementClass, elementId, versionId);

        return new VersionAggregate(
            this.getVersionInfo(rootNode),
            this.getAggregateNode(rootNode)
        );
    }


    private VersionData getVersionInfo(XmlNodeInfo nodeInfo) {
        if (nodeInfo.getChildNodes().isEmpty()) {
            return VersionData.ETERNAL_VERSION;
        }

        XmlNodeInfo versionNode = nodeInfo.getChildNodes().get(0);
        String id = versionNode.getAttributes().get(XmlRepoService.ID_ATTRIBUTE_NAME);
        String gueltigVon = versionNode.getAttributes().get(XmlRepoService.VERSION_VON_ATTRIBUTE_NAME);
        String gueltigBis = versionNode.getAttributes().get(XmlRepoService.VERSION_BIS_ATTRIBUTE_NAME);

        if (id == null || gueltigVon == null || gueltigBis == null) {
            return VersionData.ETERNAL_VERSION;
        }

        return new VersionData(
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
