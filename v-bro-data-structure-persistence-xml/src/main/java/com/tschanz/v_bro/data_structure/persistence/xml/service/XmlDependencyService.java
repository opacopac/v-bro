package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.repo.persistence.xml.parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlDependencyService implements DependencyService {
    private final XmlRepoService repoService;
    private final XmlVersionService versionService;
    private final XmlVersionAggregateService versionAggregateService;


    @Override
    public List<FwdDependency> readFwdDependencies(@NonNull VersionData version) throws RepoException {
        Map<String, XmlIdElementPosInfo> elementLutInfos = this.repoService.getElementLut();
        VersionAggregate versionAggregate = this.versionAggregateService.readVersionAggregate(version);
        List<FwdDependency> fwdDependencies = this.findNodeDependencies(versionAggregate.getRootNode(), version.getElement().getId(), elementLutInfos);

        return fwdDependencies;
    }


    private List<FwdDependency> findNodeDependencies(AggregateNode aggregateNode, String elementId, Map<String, XmlIdElementPosInfo> elementLutInfos) throws RepoException {
        List<FwdDependency> fwdDependencies = new ArrayList<>();

        List<XmlIdElementPosInfo> fwdElements = aggregateNode.getFieldValues()
            .stream()
            .filter(keyValue -> !keyValue.key.equals(XmlRepoService.ID_ATTRIBUTE_NAME)) // filter own element/version id
            .filter(keyValue -> XmlRepoService.isId(keyValue.value))
            .flatMap(keyValue -> List.of(keyValue.value.split(" ")).stream())
            .map(elementLutInfos::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        for (XmlIdElementPosInfo fwdElement: fwdElements) {
            var elementClass = new ElementClass(fwdElement.getName());
            var element = new ElementData(elementClass, fwdElement.getElementId(), Collections.emptyList());
            var versions = this.versionService.readVersions(element);
            var fwdDependency = new FwdDependency(elementClass, element, versions);
            fwdDependencies.add(fwdDependency);
        }

        for (AggregateNode childNode: aggregateNode.getChildNodes()) {
            fwdDependencies.addAll(this.findNodeDependencies(childNode, elementId, elementLutInfos));
        }

        return fwdDependencies;
    }
}
