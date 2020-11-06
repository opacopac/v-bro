package com.tschanz.v_bro.app.usecase.select_dependency_version;

import com.tschanz.v_bro.app.usecase.common.converter.*;
import com.tschanz.v_bro.app.usecase.select_dependency_version.requestmodel.SelectDependencyVersionRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_version.responsemodel.SelectDependencyVersionResponse;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.versions.domain.model.VersionData;
import com.tschanz.v_bro.versions.domain.model.VersionFilter;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class SelectDependencyVersionUseCaseImpl implements SelectDependencyVersionUseCase {
    private final Logger logger = Logger.getLogger(SelectDependencyVersionUseCaseImpl.class.getName());
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
        this.logger.info("UC: select version '" + request.versionId + "'...");

        try {
            ElementClassService elementClassService = this.elementClassServiceProvider.getService(request.repoType);
            List<Denomination> denominations = elementClassService.readDenominations(request.elementClass);

            ElementService elementService = this.elementServiceProvider.getService(request.repoType);
            List<ElementData> elements = elementService.readElements(request.elementClass, Collections.emptyList());

            VersionService versionService = this.versionServiceProvider.getService(request.repoType);
            List<VersionData> versions = versionService.readVersionTimeline(request.elementClass, request.elementId);

            VersionFilter selectedVersionFilter = VersionFilterConverter.fromRequest(request.versionFilter);
            VersionFilter effectiveVersionFilter = selectedVersionFilter.cropToVersions(versions);

            DependencyService dependencyService = this.dependencyServiceProvider.getService(request.repoType);
            List<FwdDependency> fwdDependencies = dependencyService.readFwdDependencies(request.elementClass, request.elementId, request.versionId);

            VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(request.repoType);
            VersionAggregate versionAggregate = versionAggregateService.readVersionAggregate(request.elementClass, request.elementId, request.versionId);

            String message = "successfully read " + fwdDependencies.size() + " FWD dependencies and version aggregate";
            this.logger.info(message);

            SelectDependencyVersionResponse response = new SelectDependencyVersionResponse(
                request.elementClass,
                request.elementId,
                request.versionId,
                DenominationConverter.toResponse(denominations),
                ElementConverter.toResponse(elements),
                VersionFilterConverter.toResponse(effectiveVersionFilter),
                VersionConverter.toResponse(versions),
                FwdDependencyConverter.toResponse(fwdDependencies),
                VersionAggregateConverter.toResponse(versionAggregate),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading dependencies and version aggregate: " + exception.getMessage();
            this.logger.severe(message);

            SelectDependencyVersionResponse response = new SelectDependencyVersionResponse(null, null, null,null, null, null, null,null, null, message, true);
            this.presenter.present(response);
        }
    }
}
