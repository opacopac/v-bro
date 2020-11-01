package com.tschanz.v_bro.dependencies.persistence.xml.service;

import com.tschanz.v_bro.common.KeyValue;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.version_aggregates.domain.model.AggregateNode;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.persistence.xml.service.XmlVersionAggregateService;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.persistence.xml.service.XmlVersionService;

import java.util.*;
import java.util.stream.Collectors;


public class XmlDependencyService implements DependencyService {
    private final XmlRepoService repoService;
    private final XmlVersionService versionService;
    private final XmlVersionAggregateService versionAggregateService;


    public XmlDependencyService(
        XmlRepoService repoService,
        XmlVersionService versionService,
        XmlVersionAggregateService versionAggregateService
    ) {
        this.repoService = repoService;
        this.versionService = versionService;
        this.versionAggregateService = versionAggregateService;
    }


    @Override
    public Collection<FwdDependency> readFwdDependencies(String elemenClass, String elementId, String versionId) throws RepoException {
        Map<String, XmlElementLutInfo> elementLutInfos = this.repoService.getElementLut();
        VersionAggregate versionAggregate = this.versionAggregateService.readVersionAggregate(elemenClass, elementId, versionId);
        List<FwdDependency> fwdDependencies = this.findNodeDependencies(versionAggregate.getRootNode(), elementId, elementLutInfos);

        return fwdDependencies;
    }


    private List<FwdDependency> findNodeDependencies(AggregateNode aggregateNode, String elementId, Map<String, XmlElementLutInfo> elementLutInfos) throws RepoException {
        List<FwdDependency> fwdDependencies = new ArrayList<>();

        List<XmlElementLutInfo> fwdElements = aggregateNode.getFieldValues()
            .stream()
            .filter(keyValue -> !keyValue.key.equals(XmlRepoService.ID_ATTRIBUTE_NAME)) // filter own element/version id
            .filter(keyValue -> XmlRepoService.isId(keyValue.value))
            .flatMap(keyValue -> List.of(keyValue.value.split(" ")).stream())
            .map(elementLutInfos::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        for (XmlElementLutInfo fwdElement: fwdElements) {
            List<VersionInfo> versions = this.versionService.readVersionTimeline(fwdElement.getName(), fwdElement.getElementId());
            fwdDependencies.add(new FwdDependency(fwdElement.getName(), fwdElement.getElementId(), versions));
        }

        for (AggregateNode childNode: aggregateNode.getChildNodes()) {
            fwdDependencies.addAll(this.findNodeDependencies(childNode, elementId, elementLutInfos));
        }

        return fwdDependencies;
    }
}
