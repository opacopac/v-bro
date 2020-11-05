package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.usecase.common.responsemodel.*;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionPresenter;
import com.tschanz.v_bro.app.usecase.select_dependency_version.responsemodel.SelectDependencyVersionResponse;

import java.util.List;
import java.util.stream.Collectors;


public class SelectDependencyVersionPresenterImpl implements SelectDependencyVersionPresenter {
    private final MainModel mainModel;


    public SelectDependencyVersionPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectDependencyVersionResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            // TODO: only refresh when different
            this.mainModel.elementDenominations.next(this.getDenominationItems(response.denominations));
            this.mainModel.elements.next(this.getElementItems(response.elements));
            this.mainModel.effectiveVersionFilter.next(this.getVersionFilterItem(response.effectiveVersionFilter));
            // TODO: auto-select version
            this.mainModel.versions.next(this.getVersionItems(response.versions));
            this.mainModel.fwdDependencies.next(this.getFwdDependencyItems(response.fwdDependencies));
            this.mainModel.versionAggregate.next(this.getVersionAggregateItem(response.versionAggregate));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }


    private List<DenominationItem> getDenominationItems(List<DenominationResponse> denominations) {
        return denominations
            .stream()
            .map(denomination -> new DenominationItem(denomination.name))
            .collect(Collectors.toList());
    }


    private List<ElementItem> getElementItems(List<ElementResponse> elements) {
        return elements
            .stream()
            .map(element -> new ElementItem(element.id, element.name))
            .collect(Collectors.toList());
    }


    private List<FwdDependencyItem> getFwdDependencyItems(List<FwdDependencyResponse> fwdDependencies) {
        return fwdDependencies
            .stream()
            .map(dependency -> new FwdDependencyItem(
                dependency.elementClass,
                dependency.elementId,
                this.getVersionItems(dependency.versions)
            ))
            .collect(Collectors.toList());
    }


    private VersionFilterItem getVersionFilterItem(VersionFilterResponse versionFilter) {
        return new VersionFilterItem(
            versionFilter.minGueltigVon,
            versionFilter.maxGueltigBis,
            versionFilter.minPflegestatus
        );
    }


    private List<VersionItem> getVersionItems(List<VersionResponse> versions) {
        return versions
            .stream()
            .map(version -> new VersionItem(version.id, version.gueltigVon, version.gueltigBis))
            .collect(Collectors.toList());
    }


    private VersionAggregateItem getVersionAggregateItem(VersionAggregateResponse versionAggregate) {
        return new VersionAggregateItem(
            this.getAggregateNodeItem(versionAggregate.rootNode)
        );
    }


    private AggregateNodeItem getAggregateNodeItem(VersionAggregateNodeResponse node) {
        return new AggregateNodeItem(
            node.nodeName,
            node.fieldValues
                .stream()
                .map(keyValue -> new FieldAggregateNodeItem(keyValue.key, keyValue.value))
                .collect(Collectors.toList()),
            node.childNodes
                .stream()
                .map(this::getAggregateNodeItem)
                .collect(Collectors.toList())
        );
    }
}
