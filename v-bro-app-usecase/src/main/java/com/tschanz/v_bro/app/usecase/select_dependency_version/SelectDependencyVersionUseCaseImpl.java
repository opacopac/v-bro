package com.tschanz.v_bro.app.usecase.select_dependency_version;

import com.tschanz.v_bro.app.usecase.common.converter.*;
import com.tschanz.v_bro.app.usecase.select_dependency_version.requestmodel.SelectDependencyVersionRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_version.responsemodel.SelectDependencyVersionResponse;
import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.*;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;


@Log
public class SelectDependencyVersionUseCaseImpl implements SelectDependencyVersionUseCase {
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final SelectDependencyVersionPresenter presenter;


    public SelectDependencyVersionUseCaseImpl(
        RepoServiceProvider<ElementClassService> elementClassServiceProvider,
        RepoServiceProvider<ElementService> elementServiceProvider,
        RepoServiceProvider<VersionService> versionServiceProvider,
        RepoServiceProvider<DependencyService> dependencyServiceProvider,
        RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider,
        SelectDependencyVersionPresenter presenter
    ) {
        this.elementClassServiceProvider = elementClassServiceProvider;
        this.elementServiceProvider = elementServiceProvider;
        this.versionServiceProvider = versionServiceProvider;
        this.dependencyServiceProvider = dependencyServiceProvider;
        this.versionAggregateServiceProvider = versionAggregateServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectDependencyVersionRequest request) {
        log.info("UC: select dependency version '" + request.versionId + "'...");

        try {
            // element denominations
            ElementClassService elementClassService = this.elementClassServiceProvider.getService(request.repoType);
            List<Denomination> denominations = elementClassService.readDenominations(request.elementClass);
            List<String> selectDenominations = denominations.size() > 0 ? List.of(denominations.get(0).getName()) : Collections.emptyList();

            // elements
            ElementService elementService = this.elementServiceProvider.getService(request.repoType);
            List<ElementData> elements = elementService.readElements(request.elementClass, selectDenominations, "", 100);

            // versions
            VersionService versionService = this.versionServiceProvider.getService(request.repoType);
            List<VersionData> versions = versionService.readVersionTimeline(request.elementClass, request.elementId);

            // version filter
            VersionFilter selectedVersionFilter = VersionFilterConverter.fromRequest(request.versionFilter);
            VersionFilter effectiveVersionFilter = selectedVersionFilter.cropToVersions(versions);

            // dependencies
            DependencyService dependencyService = this.dependencyServiceProvider.getService(request.repoType);
            List<FwdDependency> fwdDependencies = dependencyService.readFwdDependencies(request.elementClass, request.elementId, request.versionId);

            // version aggregate
            VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(request.repoType);
            VersionAggregate versionAggregate = versionAggregateService.readVersionAggregate(request.elementClass, request.elementId, request.versionId);

            String message = "successfully read " + elements.size()  + " elements, " + versions.size()  + " versions, "
                + fwdDependencies.size() + " dependencies and the version aggregate";
            log.info(message);

            SelectDependencyVersionResponse response = new SelectDependencyVersionResponse(
                DenominationConverter.toResponse(denominations),
                ElementConverter.toResponse(elements),
                VersionConverter.toResponse(versions),
                VersionFilterConverter.toResponse(effectiveVersionFilter),
                FwdDependencyConverter.toResponse(fwdDependencies),
                VersionAggregateConverter.toResponse(versionAggregate),
                request.elementClass,
                selectDenominations,
                request.elementId,
                request.versionId,
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading dependency version: " + exception.getMessage();
            log.severe(message);

            SelectDependencyVersionResponse response = new SelectDependencyVersionResponse(null, null, null, null,null, null, null, null,null, null, message, true);
            this.presenter.present(response);
        }
    }
}
